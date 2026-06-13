package cz.dominik.ApprovalWorkflow;

import cz.dominik.ApprovalWorkflow.entity.*;
import cz.dominik.ApprovalWorkflow.exception.InvalidRequestStateException;
import cz.dominik.ApprovalWorkflow.exception.ResourceNotFoundException;
import cz.dominik.ApprovalWorkflow.repository.ApprovalRequestRepository;
import cz.dominik.ApprovalWorkflow.repository.UserRepository;
import cz.dominik.ApprovalWorkflow.service.ApprovalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceTest {

    @Mock
    private ApprovalRequestRepository approvalRequestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApprovalService approvalService;

    private User creator;
    private User approver;
    private ApprovalRequest pendingRequest;

    @BeforeEach
    void setUp() {
        creator = new User();
        creator.setId(1L);
        creator.setName("Dominik");
        creator.setEmail("dominik@seznam.cz");
        creator.setRole(Role.USER);

        approver = new User();
        approver.setId(2L);
        approver.setName("Petr");
        approver.setEmail("petr@seznam.cz");
        approver.setRole(Role.APPROVER);

        pendingRequest = new ApprovalRequest(creator, "Test žádost", "Popis žádosti");
        pendingRequest.setId(1L);
        pendingRequest.setRequestStatus(RequestStatus.PENDING);
    }

    @Test
    void createRequest_shouldReturnPendingRequest() {
        when(approvalRequestRepository.save(any())).thenReturn(pendingRequest);

        var result = approvalService.createRequest("Test žádost", "Popis žádosti", creator);

        assertEquals(RequestStatus.PENDING, result.getRequestStatus());
        assertEquals("Test žádost", result.getTitle());
    }

    @Test
    void approveRequest_shouldApproveSuccessfully() {
        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(approvalRequestRepository.save(any())).thenReturn(pendingRequest);

        var result = approvalService.approveRequest(approver, 1L);

        assertEquals(RequestStatus.APPROVED, result.getRequestStatus());
    }

    @Test
    void approveRequest_shouldThrow_whenCreatorApprovesOwnRequest() {
        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));

        assertThrows(InvalidRequestStateException.class,
                () -> approvalService.approveRequest(creator, 1L));
    }

    @Test
    void approveRequest_shouldThrow_whenAlreadyApproved() {
        pendingRequest.setRequestStatus(RequestStatus.APPROVED);
        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));

        assertThrows(InvalidRequestStateException.class,
                () -> approvalService.approveRequest(approver, 1L));
    }

    @Test
    void rejectRequest_shouldRejectSuccessfully() {
        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(approvalRequestRepository.save(any())).thenReturn(pendingRequest);

        var result = approvalService.rejectRequest(approver, 1L);

        assertEquals(RequestStatus.REJECTED, result.getRequestStatus());
    }

    @Test
    void rejectRequest_shouldThrow_whenAlreadyRejected() {
        pendingRequest.setRequestStatus(RequestStatus.REJECTED);
        when(approvalRequestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));

        assertThrows(InvalidRequestStateException.class,
                () -> approvalService.rejectRequest(approver, 1L));
    }

    @Test
    void getRequestById_shouldThrow_whenNotFound() {
        when(approvalRequestRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> approvalService.getRequestById(99L));
    }
}
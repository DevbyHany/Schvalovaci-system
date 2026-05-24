package cz.dominik.ApprovalWorkflow.service;

import cz.dominik.ApprovalWorkflow.dto.ApprovalRequestResponseDTO;
import cz.dominik.ApprovalWorkflow.dto.UserResponseDTO;
import cz.dominik.ApprovalWorkflow.entity.ApprovalRequest;
import cz.dominik.ApprovalWorkflow.entity.RequestStatus;
import cz.dominik.ApprovalWorkflow.entity.Role;
import cz.dominik.ApprovalWorkflow.entity.User;
import cz.dominik.ApprovalWorkflow.exception.InvalidRequestStateException;
import cz.dominik.ApprovalWorkflow.exception.ResourceNotFoundException;
import cz.dominik.ApprovalWorkflow.repository.ApprovalRequestRepository;
import cz.dominik.ApprovalWorkflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ApprovalService {

    private ApprovalRequestRepository approvalRequestRepository;

    private UserRepository userRepository;

    public ApprovalService(ApprovalRequestRepository approvalRequestRepository, UserRepository userRepository) {
        this.approvalRequestRepository = approvalRequestRepository;
        this.userRepository = userRepository;
    }

    public ApprovalRequestResponseDTO createRequest(String title, String description, User creator) {
        ApprovalRequest request = new ApprovalRequest(creator, title, description);
        request.setCreatedAt(LocalDateTime.now());
        request.setRequestStatus(RequestStatus.PENDING);
        return toDTO(approvalRequestRepository.save(request));
    }

    public ApprovalRequestResponseDTO approveRequest(User approver, Long requestId) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Žádost nenalezena"));

        if (request.getCreator().getId().equals(approver.getId())) {
            throw new InvalidRequestStateException("Nemůžeš schválit vlastní žádost");
        } else if (request.getRequestStatus().equals(RequestStatus.REJECTED)) {
            throw new InvalidRequestStateException("Žádost už byla zamítnuta");
        } else if (request.getRequestStatus().equals(RequestStatus.APPROVED)) {
            throw new InvalidRequestStateException("Žádost již byla schválena");
        }
        request.setApprover(approver);
        request.setRequestStatus(RequestStatus.APPROVED);
        request.setUpdatedAt(LocalDateTime.now());
        return toDTO(approvalRequestRepository.save(request));
    }

    public ApprovalRequestResponseDTO rejectRequest(User approver, Long requestId) {
        ApprovalRequest request = approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Žádost nenalezena"));

        if (request.getCreator().getId().equals(approver.getId())) {
            throw new InvalidRequestStateException("Nemůžeš zamítnout vlastní žádost");
        } else if (request.getRequestStatus().equals(RequestStatus.REJECTED)) {
            throw new InvalidRequestStateException("Žádost již byla zamítnuta");
        } else if (request.getRequestStatus().equals(RequestStatus.APPROVED)) {
            throw new InvalidRequestStateException("Žádost již byla schválena");
        }
        request.setApprover(approver);
        request.setRequestStatus(RequestStatus.REJECTED);
        request.setUpdatedAt(LocalDateTime.now());
        return toDTO(approvalRequestRepository.save(request));
    }

    private ApprovalRequestResponseDTO toDTO(ApprovalRequest request) {
        UserResponseDTO createDTO = new UserResponseDTO(request.getCreator().getId(), request.getCreator().getName(),
                request.getCreator().getEmail(), request.getCreator().getRole());

        UserResponseDTO approverDTO = request.getApprover() != null
                ? new UserResponseDTO(request.getApprover().getId(), request.getApprover().getName(), request.getApprover().getEmail(), request.getApprover().getRole())
                : null;
        return new ApprovalRequestResponseDTO(approverDTO, createDTO, request.getRequestStatus(), request.getUpdatedAt(), request.getCreatedAt(), request.getDescription(), request.getTitle(), request.getId());
    }

    public ApprovalRequestResponseDTO getRequestById(Long requestId) {
        return toDTO(approvalRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Žádost nenalezena")));
    }

    public List<ApprovalRequestResponseDTO> getAllRequests(User user) {
        if (user.getRole() == Role.APPROVER || user.getRole() == Role.ADMIN) {
            return approvalRequestRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        } else {
            return approvalRequestRepository.findByCreator(user)
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
        }
    }
}

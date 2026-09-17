package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.ParentRequest;
import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.model.Parent;
import Rastoder.SchoolManagementSystem.repository.ParentRepository;
import org.springframework.stereotype.Service;

@Service
public class ParentService {

    private final ParentRepository parentRepository;


    public ParentService(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    public ParentResponse createParent(ParentRequest request){

        Parent parent = Parent.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .build();

        Parent saved = parentRepository.save(parent);

        return new ParentResponse(
                saved.getParentId(),
                saved.getName(),
                saved.getSurname(),
                saved.getEmail(),
                saved.getPhoneNumber()
        );
    }
}

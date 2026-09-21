package Rastoder.SchoolManagementSystem.service;

import Rastoder.SchoolManagementSystem.dto.ParentRequest;
import Rastoder.SchoolManagementSystem.dto.ParentResponse;
import Rastoder.SchoolManagementSystem.model.Parent;
import Rastoder.SchoolManagementSystem.repository.ParentRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParentService {

    private final ParentRepository parentRepository;


    public ParentService(ParentRepository parentRepository) {

        this.parentRepository = parentRepository;
    }

    private ParentResponse getParentResponse(Parent parent){
        return new ParentResponse(
                parent.getParentId(),
                parent.getName(),
                parent.getSurname(),
                parent.getEmail(),
                parent.getPhoneNumber(),
                parent.isActive()
        );
    }

    public ParentResponse createParent(ParentRequest request){

        Parent parent = Parent.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .build();

        Parent saved = parentRepository.save(parent);

        return getParentResponse(saved);
    }

    public ParentResponse findById(UUID id) {
        Parent p = parentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Parent with the id ("+id+") not found."));

        return getParentResponse(p);
    }

    public ParentResponse updateParentWithId(UUID id, ParentRequest request) {
        Parent p = parentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Parent with the id ("+id+") not found."));

        if (request.email() != null){
            p.setEmail(request.email());
        }

        if (request.phoneNumber()!= null){
            p.setPhoneNumber(request.phoneNumber());
        }

        Parent saved = parentRepository.save(p);

        return getParentResponse(saved);
    }

    public ParentResponse setParentToUnactive(UUID id) {
        Parent p = parentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Parent with the id ("+id+") not found."));

        p.setActive(false);
        Parent saved = parentRepository.save(p);
        return getParentResponse(saved);
    }
}

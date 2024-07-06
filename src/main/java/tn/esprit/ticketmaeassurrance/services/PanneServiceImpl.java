package tn.esprit.ticketmaeassurrance.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.ticketmaeassurrance.entities.Panne;
import tn.esprit.ticketmaeassurrance.entities.TypePanne;
import tn.esprit.ticketmaeassurrance.repositories.PanneRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PanneServiceImpl implements IPanneService{
    private final PanneRepository panneRepository;
    @Override
    public Panne addPanne(Panne panne) {
        return panneRepository.save(panne);
    }

    @Override
    public List<Panne> getAll() {
        return panneRepository.findAll();
    }

    @Override
    public List<Panne> getDescriptionsParEnum(String type) {
        System.out.println(type);
        return panneRepository.findByTypePanne(TypePanne.valueOf(type));
    }
}

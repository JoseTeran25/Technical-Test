package hr.kanezi.postgres.fts.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {
    private final ActorRepository actorRepository;
    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<ActorView> getAllActors() {
        return actorRepository.findAll();
    }

    /*public ActorView getAuthorById(Long id) {
        return actorRepository.findById(id).orElse(null);
    }

    public ActorView saveAuthor(ActorView author) {
        return actorRepository.save(author);
    }*/
}

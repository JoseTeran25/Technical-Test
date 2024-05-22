package hr.kanezi.postgres.fts.quotes;

import lombok.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Value
public class QuotesService {

    QuotesViewRepository quotesViewRepository;

    public List<QuotesView> getQuotes() {
        return quotesViewRepository.findAll();
    }

    public Optional<QuotesView> findById(Long quoteId) {
        return quotesViewRepository.findById(quoteId);
    }

    public List<String> getAllAuthors(){
        return quotesViewRepository.findAllAuthors();
    }

}

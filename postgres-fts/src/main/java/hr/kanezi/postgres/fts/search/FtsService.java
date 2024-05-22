package hr.kanezi.postgres.fts.search;

import lombok.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Value
public class FtsService {

    FtsDocumentRepository ftsDocumentRepository;

    FtsWordsViewRepository ftsWordsViewRepository;

    public List<FtsDocuments> search(String query, Long limit){
        return StringUtils.isEmptyOrWhitespace(query)
                ? Collections.emptyList()
                : ftsDocumentRepository.ftsSearch(query, limit);
    }

    public List<FtsDocuments> advancedSearch(String type, String movie, String actor, String keyword,String author, String genre, Long limit) {

        return ftsDocumentRepository.advancedSearch(type, movie, actor, author, genre, keyword);
    }

    public List<FtsWordView> misspellings(String query) {
        return ftsWordsViewRepository.findSimilar(query);
    }
}

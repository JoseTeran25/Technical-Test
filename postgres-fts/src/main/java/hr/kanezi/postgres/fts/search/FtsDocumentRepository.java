package hr.kanezi.postgres.fts.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FtsDocumentRepository extends JpaRepository<FtsDocuments, String> {

    @Query(value = """
    select   q.id
    ,        q.type
    ,        q.url
    ,        fts doc
    --,        ts_headline(title, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(title, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(description, websearch_to_tsquery('simple', :q), 'startSel=<em> stopSel=</em>') as description
    ,        ts_headline(meta, websearch_to_tsquery(:q), 'startSel=<em> stopSel=</em>') as meta
    ,        greatest(ts_rank_cd(fts, websearch_to_tsquery(:q)), ts_rank_cd(fts, websearch_to_tsquery('simple', :q))) ranking
    from     fts_documents q
    where    fts@@ websearch_to_tsquery(:q) or fts@@ websearch_to_tsquery('simple', :q)
    union all
    -- match beginning of the word id query does not contain string
    select   q.id
    ,        q.type
    ,        q.url
    ,        fts doc
    ,        ts_headline(title, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as title
    ,        ts_headline(description, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as description
    ,        ts_headline(meta, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as meta
    ,        ts_rank_cd(fts, to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*')||':*')) ranking
    from     fts_documents q
    where    fts@@ to_tsquery(regexp_substr(:q,  '([[:word:]]|[[:digit:]])*') ||':*')
    and      :q ~* '^([[:word:]]|[[:digit:]])*$'
    order by ranking desc limit :limit

    """ , nativeQuery = true)
    List<FtsDocuments> ftsSearch(@Param("q") String query, @Param("limit") Long limit);

    @Query(value = """

            WITH filtered_documents AS (
                SELECT q.id,
                       q.type,
                       q.url,
                       q.fts,
        		q.title,
        		q.description,
        		q.meta,
        			greatest(ts_rank_cd(fts, websearch_to_tsquery(:keyword)), ts_rank_cd(fts, websearch_to_tsquery('simple', :keyword))) ranking
                                                         
        		
                       FROM fts_documents q
                WHERE (:type IS NULL OR q.type = :type)
                  AND (:movie IS NULL OR q.title ILIKE '%' || :movie || '%')
                  AND (:actor IS NULL OR q.description ILIKE '%' || :actor || '%')
                  AND (:author IS NULL OR q.meta ILIKE '%' || :author || '%')
                  AND (:genre IS NULL OR q.meta ILIKE '%' || :genre || '%')
                  AND (:keyword IS NULL OR q.fts @@ websearch_to_tsquery(:keyword))
             
                UNION ALL
             
                SELECT q.id,
                       q.type,
                       q.url,
                       q.fts,
        		ts_headline(title, to_tsquery(regexp_substr(:keyword,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as title,
        		ts_headline(description, to_tsquery(regexp_substr(:keyword,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as description,
        		ts_headline(meta, to_tsquery(regexp_substr(:keyword,  '([[:word:]]|[[:digit:]])*')||':*'), 'startSel=<em> stopSel=</em>') as meta,
        		ts_rank_cd(fts, to_tsquery(regexp_substr(:keyword,  '([[:word:]]|[[:digit:]])*')||':*')) ranking
                                                       
                       FROM fts_documents q
                WHERE q.fts @@ to_tsquery(regexp_substr(:keyword, '([[:word:]]|[[:digit:]])*') || ':*')
                  AND :keyword ~* '^([[:word:]]|[[:digit:]])*$'
            )
            SELECT id, type, url, fts as doc, title, description, meta, ranking
            FROM filtered_documents
            ORDER BY ranking DESC;       


""", nativeQuery = true)
    List<FtsDocuments> advancedSearch(@Param("type") String type,
                                      @Param("movie") String movie,
                                      @Param("actor") String actor,
                                      @Param("author") String author,
                                      @Param("genre") String genre,
                                      @Param("keyword") String keyword);

}

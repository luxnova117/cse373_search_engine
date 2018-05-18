   package search.analyzers;

import datastructures.concrete.ChainedHashSet;

import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;


/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;

    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    private IDictionary<URI, Double> allPageNorms;
    // Feel free to add extra fields and helper methods.
    
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        System.out.println("Start Analyze");
        this.idfScores = this.computeIdfScores(webpages);
        System.out.println("Finish IDF Scores");
        allPageNorms = new ChainedHashDictionary<URI, Double>();
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        for (KVPair<URI, IDictionary<String, Double>> pair : this.documentTfIdfVectors) {
            //System.out.println(pair.getValue() == null);
            allPageNorms.put(pair.getKey(), norm(pair.getValue()));
        }
    }

    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: feel free to change or modify these methods however you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Double> idfScore = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Integer> wordWithPage = new ChainedHashDictionary<String, Integer>();
        int docNum = pages.size();
        // find every single unique word and determine how many pages contain each unique word
        for (Webpage page : pages) {
            IList<String> words = page.getWords();
            ISet<String> uniques = new ChainedHashSet<String>();
            for (String term : words) {
                if (!uniques.contains(term)) {
                    if (wordWithPage.containsKey(term)) {
                        wordWithPage.put(term, wordWithPage.get(term) + 1);
                    } else {
                        wordWithPage.put(term, 1);
                    }
                }
                uniques.add(term);
                
            }
        }
        
        // calculate Idf score
        for (KVPair<String, Integer> wordAndPageNum : wordWithPage) {
            idfScore.put(wordAndPageNum.getKey(), Math.log((docNum + 0.0) / (wordAndPageNum.getValue() + 0.0)));
        }
        return idfScore;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computetfScores(IList<String> words) {
        IDictionary<String, Double> uniqueTerms = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> tfScore = new ChainedHashDictionary<String, Double>();
        double wordCount = words.size() + 0.0;
        if (wordCount < 1) {
            return tfScore;
        }
        
        // count how many times a word appears
        for (String word : words) {
            if (!uniqueTerms.containsKey(word)) {
                uniqueTerms.put(word, 1.0);
            } else {
                uniqueTerms.put(word, uniqueTerms.get(word) + 1);
            }
        }
        
        // calculate tf score
        // edit: fixed concurrent mod exception
        
        for (KVPair<String, Double> term : uniqueTerms) {
            tfScore.put(term.getKey(), term.getValue() / wordCount);
        }
     
        return tfScore;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computetfScores(...) method.
        
        IDictionary<URI, IDictionary<String, Double>> allVectors = 
                new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage page : pages) {
            IDictionary<String, Double> tfScores = computetfScores(page.getWords());
            IDictionary<String, Double> vectors = new ChainedHashDictionary<String, Double>();
            for (KVPair<String, Double> score : tfScores) {
                String term = score.getKey();
                vectors.put(term, idfScores.get(term) * score.getValue());
            }
            allVectors.put(page.getUri(), vectors);
            
        }
        return allVectors;
    }
    
    
    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // Note: The pseudocode we gave you is not very efficient. When implementing,
        // this method, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.

        IDictionary<String, Double> pageVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<String, Double>();
        IDictionary<String, Double> queryTfScores = computetfScores(query);
        double numerator = 0.0;
        
        for (KVPair<String, Double> score : queryTfScores) {
            double pageWordScore;
            String term = score.getKey();
            if (pageVector.containsKey(term)) {
                pageWordScore = pageVector.get(term);
            } else {
                pageWordScore = 0.0;
            }
            double queryWordScore;
            if (idfScores.containsKey(term)) {
                queryWordScore = score.getValue() * idfScores.get(term);
            } else {
                queryWordScore = 0.0;
            }
            
            queryVector.put(term, queryWordScore);
            numerator += pageWordScore * queryWordScore;
        }
        
        double denominator = allPageNorms.get(pageUri) * norm(queryVector);
        
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
    }
    
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}

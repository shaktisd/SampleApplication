import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.hcoref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.CoreMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class NewCorefExample {

  public static void main(String[] args) throws Exception {
    String input = new String(Files.readAllBytes(Paths.get("src/input.txt")));
    Annotation document = new Annotation(input);
    Properties props = new Properties();
    //props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,mention,dcoref");
    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,coref");
    
    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
    pipeline.annotate(document);
    System.out.println("---");
    System.out.println("coref chains");
    for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
      System.out.println("\t"+cc);
    }
    for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
	    List<CoreLabel> tokens = sentence.get(TokensAnnotation.class); 
	    SemanticGraph dependencies = sentence.get(BasicDependenciesAnnotation.class);
	    int tokenIdxInSentence = 0; 
	    for(CoreLabel token : tokens){
	    	String pos = token.get(PartOfSpeechAnnotation.class);	
	    	String ne = token.get(NamedEntityTagAnnotation.class); 
	    	String lemma = token.get(LemmaAnnotation.class);
	    	IndexedWord vertex = dependencies.getNodeByIndexSafe(++tokenIdxInSentence);
	    	List<SemanticGraphEdge> edges = new ArrayList<SemanticGraphEdge>(); 
	        edges.addAll(dependencies.incomingEdgeList(vertex)); 
	        edges.addAll(dependencies.outgoingEdgeList(vertex)); 
	        
	        if (vertex == null) { 
	            // Usually the current token is a punctuation mark in this case. 
	            continue; 
	        }
	        for (SemanticGraphEdge edge : edges) { 
	            int govIndex = edge.getGovernor().index(); 
	            int depIndex = edge.getDependent().index(); 
	            GrammaticalRelation gramRel = edge.getRelation();
	            if (govIndex == tokenIdxInSentence) { 
	                CoreLabel dependentLabel = tokens.get(depIndex - 1); 
	            } else if (depIndex == tokenIdxInSentence) { 
	                boolean isDependent = true; 
	                CoreLabel governorLabel = tokens.get(govIndex - 1); 
	            }
	        }
	     // Finally add the root relation if the word has any. 
	        Collection<IndexedWord> roots = dependencies.getRoots();
	    }
	    
	    
      System.out.println("---");
      System.out.println("mentions");
      for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
        System.out.println("\t"+m);
       }
    }
  }
}
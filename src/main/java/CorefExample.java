import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.hcoref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.IntTuple;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class CorefExample {

    public static void main(String[] args) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get("src/input.txt")));
        Annotation document = new Annotation(input);
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        pipeline.annotate(document);
        System.out.println("---");
        System.out.println("coref chains");
        for (CorefChain cc : document.get(CorefCoreAnnotations.CorefChainAnnotation.class).values()) {
            if(cc.getMentionsInTextualOrder().size() != 0){
                System.out.println("\t" + cc);
                for (CorefChain.CorefMention cm : cc.getMentionsInTextualOrder()) {
                    String textOfMention = cm.mentionSpan;
                    IntTuple positionOfMention = cm.position;

                    System.out.println("textOfMention : " + textOfMention);
                    // System.out.println("positionOfMention : " + positionOfMention);
                }
            }
        }
        for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
            System.out.println("---");

        }
    }

}
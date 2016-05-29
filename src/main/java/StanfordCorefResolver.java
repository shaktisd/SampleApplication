import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * Created by Shakti on 5/29/2016.
 */
public class StanfordCorefResolver {

    public StanfordCoreNLP createPipeline(){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        return pipeline;
    }
    //path = "src/input.txt"
    public void resolve(String path,StanfordCoreNLP pipeline) throws Exception {
        String input = new String(Files.readAllBytes(Paths.get(path)));
        Annotation document = new Annotation(input);
        pipeline.annotate(document);
        final List<CoreMap> coreMaps = document.get(SentencesAnnotation.class);

    }
}

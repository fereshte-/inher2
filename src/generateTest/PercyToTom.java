package generateTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import lambda.Lang;
import lambda.PType;
import learn.DataSet;
import utils.LispReader;

public class PercyToTom {
	
	private class Triple{
		String original, formula, utterence;
		public Triple(String original, String formula, String utterence) {
			this.original = original;
			this.formula = formula;
			this.utterence = utterence;
		}
	}

	String change(String utterence){
		utterence = utterence.replace("?", " ");
		utterence = utterence.replace("-", " ");
		utterence = utterence.replace("'s", " 's");
		utterence = utterence.replace("weekly standup", "weekly_standup");
		utterence = utterence.replace("annual review", "annual_review");
		utterence = utterence.replace("greenberg cafe", "greenberg_cafe");
		utterence = utterence.replace("central office", "central_office");
		
		utterence = utterence.replace("multivariate data analysis",
				"multivariate_data_analysis");
		utterence = utterence.replace("annals of statistics", "annals_of_statistics");
		utterence = utterence.replace("computational linguistics", "computational_linguistics");
		
		return utterence;
	}
	
	public List<Triple> percy_toGeo(String fileName, String outName) throws FileNotFoundException{
		
		List triples = new LinkedList<Triple>();
		int won_award = 0;
		int venue =0;
		int pub_date =0;
		int author =0;
		
		int is_important = 0;
		int location =0;
		int date=0;
		int attendee = 0;
		// detemine if it is a function application or a literal
		LispReader wholeFileLr = new LispReader(new FileReader(fileName));
		while(wholeFileLr.hasNext()){
			String wholeSentence = wholeFileLr.next();
			LispReader wholeSentenceLr = new LispReader(new StringReader(wholeSentence));
			wholeSentenceLr.next();	//passing the example
			String utterence = wholeSentenceLr.next();
			LispReader utterenceLr = new LispReader(new StringReader(utterence));
			utterenceLr.next();
			utterence = "";
			while(utterenceLr.hasNext())
				utterence += " " + utterenceLr.next();
			utterence = utterence.replaceAll("\"", "").trim();
			
			String original = wholeSentenceLr.next();
			LispReader originalLr = new LispReader(new StringReader(original));
			originalLr.next();
			original = "";
			while(originalLr.hasNext())
				original += " " + originalLr.next();	
			original = original.replaceAll("\"", "").trim();

			String formula = wholeSentenceLr.next();
			LispReader formulaLr = new LispReader(new StringReader(formula));
			formulaLr.next();
			formula = formulaLr.next();

			formula = formula.replaceAll("call edu.stanford.nlp.sempre.{1,15}.SimpleWorld.", "");
			formula = formula.replaceAll("call .size", "ccount");
			formula = formula.replaceAll("!type", "type");
			
			utterence = change (utterence);
			original = change(original);
			
			
// calandar
			
			formula = formula.replace("en.meeting.weekly_standup", "weekly_standup:me");
			formula = formula.replace("en.meeting.annual_review", "annual_review:me");
			formula = formula.replace("en.location.greenberg_cafe", "greenberg_cafe:lo");
			formula = formula.replace("en.location.central_office", "central_office:lo");
			formula = formula.replace("en.person.alice", "alice:pe");
			formula = formula.replace("en.person.bob", "bob:pe");


			formula = formula.replace("en.hour", "hour:tyho");
//			formula = formula.replace("en.person", "person:type");
			formula = formula.replace("en.meeting", "meeting:tyme");
			formula = formula.replace("en.location", "location:tylo");

//publication
			
			formula = formula.replace("en.article.multivariate_data_analysis",
					"multivariate_data_analysis:ar");
			formula = formula.replace("en.venue.annals_of_statistics",
					"annals_of_statistics:ve");
			formula = formula.replace("en.venue.computational_linguistics",
					"computational_linguistics:ve");
			formula = formula.replace("en.person.efron", "efron:pe");
			formula = formula.replace("en.person.lakoff", "lakoff:pe");


			formula = formula.replace("en.article", "article:tyar");
			formula = formula.replace("en.person", "person:type");
			formula = formula.replace("en.venue", "venue:tyve");
			
		//	formula = formula.replace("concat", "or");
			
			


			if(
					formula.contains("<=") || formula.contains(">=") ||
					formula.contains("countS") || formula.contains("countC") 
					|| formula.contains("min") || formula.contains("max")
					|| formula.contains("cites") || formula.contains("length")
					|| formula.contains("count") 
					|| formula.contains("won_award") || formula.contains("is_important")
					|| formula.contains("start_time") || formula.contains("end_time")
					) continue;
			
			
			if(outName.contains("test")){
//				if(formula.contains("attendee")){
//					if(attendee > 0 )	continue;
//					else	attendee++;
//				}
//				if(formula.contains("location")){
//					if(location > 0 )	continue;
//					else	location++;
//				}
//				if(formula.contains("(date")){
//					if(date > 0 )	continue;
//					else	date++;
//				}
//				if(formula.contains("is_important")){
//					if(is_important > 0 )	continue;
//					else	is_important++;
//				}
				//out.println(utterence + "\n" + formula + "\n");
				triples.add(new Triple(original, formula, utterence));

			}else{
//				if(formula.contains("author")){
//					if(author > 2 )	continue;
//					else	author++;
//				}
//				if(formula.contains("venue")){
//					if(venue > 2 )	continue;
//					else	venue++;
//				}
//				if(formula.contains("publication_date")){
//					if(pub_date > 2 )	continue;
//					else	pub_date++;
//				}
//				if(formula.contains("won_award")){
//					if(won_award > 2 )	continue;
//					else	won_award++;
//				}
				//out.println(utterence + "\n" + formula + "\n");
				triples.add(new Triple(original, formula, utterence));
			}

		}
		return triples;
	
	}
	
	public void percent(String one, String two, String outName, double perOne, double perTwo) throws FileNotFoundException{
		List <Triple> l1 = percy_toGeo(one, outName);
		List <Triple> l2 = percy_toGeo(two, outName);
		
		Collections.shuffle(l1);
		Collections.shuffle(l2);

		int size = l1.size();
		l1.subList((int) (perOne * size), size).clear();
		int size2=l2.size();
		l2.subList((int) (perTwo * size2), size2).clear();
		
		
		l1.addAll(l2);
		Collections.shuffle(l1);
		PrintWriter out = new PrintWriter(outName);
		for(Triple x : l1){
			out.println(x.original + "\n" + x.formula + "\n");
		//	out.println(x.utterence+ "\n" + x.formula + "\n");
		}
		out.close();
	}
	
	public static void print(DataSet dataset, String fileName) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter out = new PrintWriter(fileName, "UTF-8");
		for(int i=0; i<dataset.size(); i++){
			out.println(dataset.sent(i));
			out.println(dataset.sem(i));
			out.println();
		}
		out.close();

	}
	
	public static void main(String[] args) throws IOException {
		PercyToTom percyToTom = new PercyToTom();
		
//		percyToTom.percy_toGeo("./data/real_train_both.txt",
//				"./data/train.txt");
//		percyToTom.percy_toGeo("./data/real_test_both.txt",
//				"./data/test.txt");
		
		PType.addTypesFromFile("geo-lambda.types");
		Lang.loadLangFromFile("geo-lambda.lang");
		
		percyToTom.percent("../../data/real_train_calandar.txt",
				"../../data/real_train_publication.txt" , "../../data/train.txt", 1, 0.2);
		DataSet train = new DataSet("../../data/train.txt");
		print(train, "../../data/train.txt");
		
		percyToTom.percent("../../data/real_test_calandar.txt",
				"../../data/real_test_publication.txt" , "../../data/test.txt", 0, 1);
		DataSet test = new DataSet("../../data/test.txt");
		print(test, "../../data/test.txt");

	}
}

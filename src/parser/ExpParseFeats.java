/***************************  LICENSE  *******************************
 * This file is part of UBL.
 * 
 * UBL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * UBL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with UBL.  If not, see <http://www.gnu.org/licenses/>.
 ***********************************************************************/



package parser;

import java.util.*;
import lambda.*;

public class ExpParseFeats implements ParseFeatureSet {
	
	 public static boolean share = true;
	
	public void setFeats(Cell c, List<Cell> children, HashVector feats){
		if (c.getStart()!=0 || c.getEnd()!=Globals.lastWordIndex) 
			return;
		setFeats(c.getCat().getSem(),feats);
	}

	public void setFeats(Exp sem, HashVector feats){

		//System.out.println("feats: "+sem);

		String index = "RN:"+(sem.inferType()!=null);
		feats.set(index,feats.get(index)+scale);

		//feats.set("AVG_DEPTH",sem.avgDepth()*scale);

		for (Exp e : sem.allSubExps()){
			if (e instanceof Lit){
				Lit l = (Lit)e;
				String head = l.getHeadString();
				for (int i=0; i<l.arity(); i++){
					// use cache from inferType() call above for type info
					index = "LHT:"+i+":"+head+":"+l.getArg(i).inferedType;
					feats.set(index,feats.get(index)+scale);


					index = "LHH:"+i+":"+head+":"+l.getArg(i).getHeadString();
					feats.set(index,feats.get(index)+scale);


					String parent_head = l.getPred().getParent();
					if(share && parent_head != null && !parent_head.equals("none")){
						index = "LHPT:"+i+":"+parent_head+":"+l.getArg(i).inferedType;
						feats.set(index,feats.get(index)+scale);


						index = "LHPH:"+i+":"+parent_head+":"+l.getArg(i).getHeadString();
						feats.set(index,feats.get(index)+scale);
					}

					Exp arg = l.getArg(i);
					if(share && arg instanceof Lit){
						Lit argLit = (Lit) arg;
						//	System.out.println("fereshte + " + argLit.getPred().getParent());
						if(argLit.getPred().getParent() != null){
							index = "LHP:"+i+":"+head+":"+argLit.getPred().getParent();
							//		System.out.println(" i added parent"  + index);
							feats.set(index,feats.get(index)+ scale );
						}
					}
				}
			}
			if(e instanceof BoolBoolOps){
				for (String id : ((BoolBoolOps)e).getHeadPairs()){
					index = "BB:"+id;
					feats.set(index,feats.get(index)+scale);
				}
				for (String id : ((BoolBoolOps)e).getHeadPairsParents()){
					index = "BB:"+id;
					//		System.out.println(" i added boolbool "  + index);
				if(share)	feats.set(index,feats.get(index)+ scale);
				}
			}
			if(e instanceof ArgM){
				ArgM argM = (ArgM) e;
				Exp body = argM.getBody();
				System.out.println("body is " + argM.getBody().toString());
				if(body instanceof Lit){
					Lit lbody = (Lit) body;
					String p = lbody.getPred().getParent();
					String id = argM.getHeadString() + ":";
					if(p == null){
						index = id + lbody.getHeadString();
						//			System.out.println(" i added argm "  + index);

						//	feats.set(index,feats.get(index)+scale);
					}else{
						index = id + p;
						//			System.out.println(" i added argm "  + index);
						//	feats.set(index,feats.get(index)+scale);
					}
				}
			}
		}
	}



	public double score(Cell c, List<Cell> children, HashVector theta){
		if (c.getStart()!=0 || c.getEnd()!=Globals.lastWordIndex) 
			return 0.0;
		double score = 0.0;
		Exp sem = c.getCat().getSem();

		//System.out.println("score: "+c);

		String index = "RN:"+(sem.inferType()!=null);
		score+=theta.get(index)*scale;

		//score+=theta.get("AVG_DEPTH")*scale*sem.avgDepth();

		for (Exp e : sem.allSubExps()){
			if (e instanceof Lit){
				Lit l = (Lit)e;
				String head = l.getHeadString();
				for (int i=0; i<l.arity(); i++){
					// use cache from inferType() call above for type info
					index = "LHT:"+i+":"+head+":"+l.getArg(i).inferedType;
					score+=theta.get(index)*scale;

					index = "LHH:"+i+":"+head+":"+l.getArg(i).getHeadString();
					score+=theta.get(index)*scale;
					
					String parent_head = l.getPred().getParent();
					if(parent_head != null && !parent_head.equals("none")){
						index = "LHPT:"+i+":"+parent_head+":"+l.getArg(i).inferedType;
						score+=theta.get(index)*scale;


						index = "LHPH:"+i+":"+parent_head+":"+l.getArg(i).getHeadString();
						score+=theta.get(index)*scale;
					}

					Exp arg = l.getArg(i);
					if(arg instanceof Lit){
						Lit argLit = (Lit) arg;
						if(argLit.getPred().getParent() != null){
							index = "LHP:"+i+":"+head+":"+argLit.getPred().getParent();
							score+=theta.get(index)*scale;
						}
					}
				}
				if(e instanceof BoolBoolOps){
					for (String id : ((BoolBoolOps)e).getHeadPairs()){
						index = "BB:"+id;
						score+=theta.get(index)* scale;
					}
					for (String id : ((BoolBoolOps)e).getHeadPairsParents()){
						index = "BB:"+id;
						score+=theta.get(index)* scale;
					}
				}
			}
		}
		return score;
	}

	public static void main(String[] args){
		PType.addTypesFromFile("../experiments/geo250-funql/geo-funql.types");
		Lang.loadLangFromFile("../experiments/geo250-funql/geo-funql.lang");

		ExpParseFeats epf = new ExpParseFeats();

		Exp e = Exp.makeExp("(answer (state (loc_1 (highest all:e))))");
		HashVector feats = new HashVector();

		epf.setFeats(e,feats);
		System.out.println(e);
		System.out.println(feats);

	}

	double scale=1.0;

}
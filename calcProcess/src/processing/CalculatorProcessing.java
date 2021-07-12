//Copyright 2020, 2021 Kevin Pember
/*
 	This file is part of Recursive.

	Recursive is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Recursive is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Recursive.  If not, see <https://www.gnu.org/licenses/>.
*/
package processing;

import ch.obermuhlner.math.big.BigDecimalMath;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class CalculatorProcessing {
	String piCon = ""+BigDecimalMath.pi(new MathContext(16));
	String eCon = ""+BigDecimalMath.e(new MathContext(16));
	public MathContext mc = new MathContext(16);
	public boolean cacMode = true;
	public ArrayList<Integer> operatorLedger;
	public CalculatorProcessing(){

	}
	public CalculatorProcessing(MathContext mc, boolean cacMode) {
		this.mc = mc;
		this.cacMode = cacMode;
		piCon = "" + BigDecimalMath.pi(mc);
		eCon = "" + BigDecimalMath.e(mc);
	}
	public String toString(){
		String mode;
		if(cacMode){
			mode = "Degrees";
		}else{
			mode = "Radians";
		}
		return "MathContext: "+mc.toString() + " calculatorMode="+mode;
	}
	public void setCacMode(boolean cacMode) {
		this.cacMode = cacMode;
	}
	// Method that is the base of the Calculator that finds operators and calls the methods needed to solve them
	public String solve(String userInput, boolean end) {
		 System.out.println(mc.toString());
		 System.out.println("Input math ran");
		 System.out.println("This is input "+userInput);
		 String resultValue ="";
	     int frontParse = 0;
	     userInput = userInput.replaceAll(" ","");
		 for(int i = 0; i < userInput.length(); i++) {
				switch(userInput.charAt(i)) {
				  case('π'):
					  System.out.println(userInput.substring(i,i+1));
					  userInput = userInput.substring(0,i) + mutiNeeded(i,i+1,userInput,piCon.substring(0,mc.getPrecision()-1)) + userInput.substring(i+1);
				  break;
				  case('e'): 
					System.out.println(userInput.substring(i,i+1));
				    if(userInput.charAt(i-1) == 's') {
				    	break;
				    }
					userInput = userInput.substring(0,i) + mutiNeeded(i,i+1,userInput,eCon.substring(0,mc.getPrecision()-1)) + userInput.substring(i+1);
				  break;
				  
				}
		 }
	    for(int i = 0; i < userInput.length(); i++) {
	   	 if (userInput.charAt(i) == '(') {
	   		 //parPos.addFirst(i);
	   		 userInput = parMethod(userInput,i);
	 	 }
	    }
	    for(int i = 0; i < userInput.length(); i++) {
		   	 if (userInput.charAt(i) == '|') {
		   		 userInput = absoluteValue(userInput,i);
		 	 }
		}
		 for(int i = 0; i < userInput.length(); i++) {
			 if(superscriptToNormal(userInput.charAt(i)) != ',') {
				 userInput = superScriptProcessing(userInput,i);
				 i = 0;
			 }
			switch(userInput.charAt(i)) {
			  case('^'):
				if(userInput.charAt(i-1) != ',') {
					try{
						String shortMem = userInput;
						System.out.println(userInput.substring(i-forward(userInput.substring(0,i)).length(),i+userInput.substring(i+1).length()+1));
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + BigDecimalMath.pow(new BigDecimal(forward(userInput.substring(0, i))), new BigDecimal(backward(userInput.substring(i + 1))), mc) + userInput.substring(i + userInput.substring(i + 1).length() + 1);
						i = i + userInput.substring(i + 1).length() + 1;
					}catch(Exception e){
						System.out.println("^ Skiped: "+e);
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "[" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "]"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i++;
					}
				}
			  break;
			  case('√'):
				if(userInput.charAt(i+1) != ',') {
					try {
						String shortMem = userInput;
						System.out.println(userInput.substring(i, i + backward(userInput.substring(i + 1)).length() + 1));
						userInput = userInput.substring(0, i) + mutiNeeded(i, i + backward(userInput.substring(i + 1)).length() + 1, userInput, "" + BigDecimalMath.sqrt(new BigDecimal(backward(userInput.substring(i + 1))), mc)) + userInput.substring(i + backward(userInput.substring(i + 1)).length() + 1);
						i = i + backward(userInput.substring(i + 1)).length() + 1;
					}catch(Exception e){
						if(e instanceof NumberFormatException) {
							System.out.println("√ Skiped: " + e);
							userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "[" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "]"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							i++;
						}
					}
				}
			  break;
			 }
		 }
		 for(int i = 0; i < userInput.length(); i++) {
			 switch(userInput.charAt(i)) {
			 case('×'):
				 if(userInput.charAt(i-1) != ',') {
				 	try {
						System.out.println((new BigDecimal(forward(userInput.substring(0, i))) + " " + (new BigDecimal(backward(userInput.substring(i + 1)), mc))));
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + (new BigDecimal(forward(userInput.substring(0, i))).multiply(new BigDecimal(backward(userInput.substring(i + 1)), mc), mc)) + userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						System.out.println("This is userInput" + userInput);
						i = i + ("" + backward(userInput.substring(i + 1))).length() + 1;
						System.out.println("i is "+i);
					}catch(Exception e){
						System.out.println("× Skiped: "+e.getLocalizedMessage());
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i++;
					}
				 }
			 break;
			 case('*'):
				 if(userInput.charAt(i-1) != ',') {
				 	try {
						System.out.println(userInput.substring(i - forward(userInput.substring(0, i)).length(), i + userInput.substring(i + 1).length() + 1));
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + (new BigDecimal(forward(userInput.substring(0, i))).multiply(new BigDecimal(backward(userInput.substring(i + 1)), mc), mc)) + userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i = i + ("" + backward(userInput.substring(i + 1))).length() + 1;
						System.out.println("i is "+i);
					}catch(Exception e){
				 		if(e instanceof NumberFormatException) {
							System.out.println("* Skiped: " + e.getLocalizedMessage());
							userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							i++;
						}
					}
				 }
			 break;
			 case('÷'):
				 System.out.println(userInput.substring(i-forward(userInput.substring(0,i)).length(),i+userInput.substring(i+1).length()+1));
			 	 if(userInput.charAt(i-1) != ',') {
			 		 try {
			 			 resultValue = ""+(new BigDecimal(forward(userInput.substring(0,i)),mc).divide(new BigDecimal(backward(userInput.substring(i+1)),mc),mc));
			 			 System.out.println(resultValue);
			 			 frontParse = i-forward(userInput.substring(0,i)).length();
			 			 userInput = userInput.substring(0,frontParse)+resultValue+userInput.substring(i+(""+backward(userInput.substring(i+1))).length()+1);
			 			 i = frontParse + resultValue.length()-1;
			 		 }catch(Exception e) {
			 			 System.out.println("Eception at ÷");
			 			 if(e instanceof ArithmeticException) {
			 				 System.out.println("Divison by Zero");
			 				 userInput = "undefined";
			 			 }else if(e instanceof NumberFormatException){
							 System.out.println("÷ Skiped: " + e.getLocalizedMessage());
							 userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							 i++;
			 			 }
			 		 }
			 	 }
			 break;
			 case('/'):
			   if(userInput.charAt(i-1) != ',') {
				   System.out.println(userInput.substring(i-forward(userInput.substring(0,i)).length(),i+userInput.substring(i+1).length()+1));
				 	 try {
				 		 resultValue = ""+(new BigDecimal(forward(userInput.substring(0,i)),mc).divide(new BigDecimal(backward(userInput.substring(i+1)),mc),mc));
				 		 System.out.println(resultValue);
				 		 frontParse = i-forward(userInput.substring(0,i)).length();
				 		 userInput = userInput.substring(0,frontParse)+resultValue+userInput.substring(i+(""+backward(userInput.substring(i+1))).length()+1);
				 		 i = frontParse + resultValue.length()-1;
				 		System.out.println("i is :"+i);
				 	 }catch(Exception e) {
						 System.out.println("Eception at ÷");
						 if(e instanceof ArithmeticException) {
							 System.out.println("Divison by Zero");
							 userInput = "undefined";
						 }else if(e instanceof NumberFormatException){
							 System.out.println("/ Skiped: " + e.getLocalizedMessage());
							 userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							 i++;
						 }
				 	 }
					 
			   }
			 break;
			 case('%'):
			 	try {
					if (userInput.charAt(i - 1) != ',') {
						System.out.println(userInput.substring(i - forward(userInput.substring(0, i)).length(), i + userInput.substring(i + 1).length() + 1));
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + (new BigDecimal(forward(userInput.substring(0, i))).multiply(new BigDecimal(backward(userInput.substring(i + 1)), mc)).divide(BigDecimal.valueOf(100), mc)) + userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i = frontParse + resultValue.length()-1;
					}
				}catch(Exception e){
					System.out.println("Eception at %");
					if(e instanceof ArithmeticException) {
						System.out.println("Divison by Zero");
						userInput = "undefined";
					}else if(e instanceof NumberFormatException){
						System.out.println("% Skiped: " + e.getLocalizedMessage());
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i++;
					}
				}
			 break;
			 case('!'):
			 	try {
					if (userInput.charAt(i - 1) != ',') {
						System.out.println(userInput.substring(i - forward(userInput.substring(0, i)).length(), i + 1));
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + mutiNeeded(i - forward(userInput.substring(0, i)).length(), i + 1, userInput, "" + factorialMethod(forward(userInput.substring(0, i)))) + userInput.substring(i + 1);
						i = frontParse + resultValue.length()-1;
					}
				}catch(Exception e){
					if(e instanceof NumberFormatException) {
						System.out.println("* Skiped: " + e.getLocalizedMessage());
						userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + "ˋ" + userInput.substring(i - forward(userInput.substring(0, i)).length(),i + ("" + backward(userInput.substring(i + 1))).length() + 1)+ "ˊ"+userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
						i++;
					}else{
						e.printStackTrace();
					}
				}
			 break;
			 
			 }
		  }	
		 for(int i = 1; i < userInput.length(); i++) {
			 switch(userInput.charAt(i)) {
			 	case('+'):
			 		if(userInput.charAt(i-1) != 'E' && userInput.charAt(i-1) != ',') {
			 			try {
							System.out.println(userInput.substring(i - forward(userInput.substring(0, i)).length(), i + userInput.substring(i + 1).length() + 1));
							userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + (new BigDecimal(forward(userInput.substring(0, i))).add(new BigDecimal(backward(userInput.substring(i + 1)), mc), mc)) + userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							i = frontParse + resultValue.length()-1;
						}catch(Exception e){
			 				if(e instanceof NumberFormatException){
								System.out.println("+ Skiped: " + e.getLocalizedMessage());
								i++;
							}
						}
			 		}
			 	break;
			 	case('-'):
			 		if(userInput.charAt(i-1) != 'E' && userInput.charAt(i-1) != ',') {
						try {
							System.out.println(userInput.substring(i - forward(userInput.substring(0, i)).length(), i + userInput.substring(i + 1).length() + 1));
							userInput = userInput.substring(0, i - forward(userInput.substring(0, i)).length()) + (new BigDecimal(forward(userInput.substring(0, i))).subtract(new BigDecimal(backward(userInput.substring(i + 1)), mc), mc)) + userInput.substring(i + ("" + backward(userInput.substring(i + 1))).length() + 1);
							i = frontParse + resultValue.length()-1;
						}catch(Exception e){
							if(e instanceof NumberFormatException){
								System.out.println("+ Skiped: " + e.getLocalizedMessage());
								i++;
							}
						}
			 		}
			 	break;
			 }
			}
		 
		 for(int i = 1; i < userInput.length(); i++) {
			 switch(userInput.charAt(i)) {
			 	case(','):
			 		userInput = userInput.substring(0,i) + userInput.substring(i+1);
			 	break;
			 	//case(')'):
			 		//userInput = userInput.substring(0,i) + userInput.substring(i+1);
			 	//break;
			 }
			}
		 
		 BigDecimal piDreriv = new BigDecimal(piCon.substring(0,mc.getPrecision()-1));
		 if(isNum(userInput)) {
			 System.out.println("Going into PI method " + userInput + " vs. "+ new BigDecimal(userInput).divide(piDreriv,mc));
			 if((""+new BigDecimal(userInput,mc)).length() > (""+new BigDecimal(userInput,mc).divide(piDreriv,mc)).length()&& Double.parseDouble(userInput) != 0 && end) {
				 System.out.println("PI Method ran");
				 if(new BigDecimal(userInput).divide(piDreriv,mc).doubleValue() == 1) {
					 userInput = "π";
				 }else {
					 userInput =  new BigDecimal(userInput).divide(piDreriv,mc) + "π";
				 }
			 }
		 }
		 return userInput;
	}

	//Method used to find the number in front of an operator
	private String forward(String sub) {
		System.out.println("Reader Back");
		 String outputSub = "";
		 for(int i = sub.length()-1; i >= 0; i--) {
			 if(sub.charAt(i) != '×' && sub.charAt(i) != '*' && sub.charAt(i) != '÷' && sub.charAt(i) != '/' && sub.charAt(i) != '√' &&sub.charAt(i) != '²' && sub.charAt(i) != '^' && sub.charAt(i) != '(' && sub.charAt(i) != ')' &&sub.charAt(i) != '%' &&sub.charAt(i) != '!'&&sub.charAt(i) != 'π'&&sub.charAt(i) != 'e'&&sub.charAt(i) != ','&&sub.charAt(i) != '|') {
				 if (i == 0 ) {
					 outputSub = sub.substring(i);
					 break; 
				 }else if(sub.charAt(i) == '+') {
					 if(sub.charAt(i-1) == 'E') {
						 
					 }else {
						 outputSub = sub.substring(i+1);
						 break;
					 }
				 }else if (sub.charAt(i) == '-') {
					 if(i == 0) {
						 
					 }else if(isOperator(sub.charAt(i-1),true)&&isOperator(sub.charAt(i-1),true)) {
						 
					 }else if(sub.charAt(i-1) == 'E'){
						 
					 }else {
						 outputSub = sub.substring(i+1);
						 break;
					 }
				 }
			 }else if(i == sub.length()-1){
				 outputSub = "";
				 break;
			 }else {
				 outputSub = sub.substring(i+1);
				 break;
			 }
		 }
		 System.out.println("Outputsub is "+ outputSub);
		 return outputSub;
	}
	//Method used to find the number behind an operator
	private String backward(String sub) {
		System.out.println("Reader Front");
		 String outputSub = "";
		 for(int i = 0; i <= sub.length()-1; i++){
	     if(sub.charAt(i) != '×' &&sub.charAt(i) != '*' &&sub.charAt(i) != '÷' &&sub.charAt(i) != '/' &&sub.charAt(i) != '√' &&sub.charAt(i) != '²' && sub.charAt(i) != '^' &&sub.charAt(i) != '(' &&sub.charAt(i) != ')' &&sub.charAt(i) != '%' &&sub.charAt(i) != '!'&&sub.charAt(i) != 'π'&&sub.charAt(i) != 'e'&&sub.charAt(i) != ','&&sub.charAt(i) != '|') {
	    	 if (i == sub.length()-1) {
					 outputSub = sub.substring(0, i + 1);
					 break; 
			 }else if(sub.charAt(i) == '+') {
				 if(sub.charAt(i-1) == 'E') {
					 
				 }else {
					 outputSub = sub.substring(0,i);
					 break;
				 }
			 }else if (sub.charAt(i) == '-') {
				 if(i == 0) {
					 
				 }else if(sub.charAt(i-1) == 'E'){
					 
				 }else {
					 outputSub = sub.substring(0,i);
					 break;
				 }
			 }
		}else if(i == 0){
			outputSub = "";
			break;
		}else {
			System.out.println("Pre asignment");
			outputSub = sub.substring(0,i);
		    break;
		}
	   }
		System.out.println("Outputsub is "+ outputSub);
		return outputSub;
	}
	//Method used to complete Pars 
	 private String parMethod(String fullInput, int firstPar) {
	 	System.out.println("Par Method Ran , Input String "+ fullInput.substring(firstPar+1));
	 	String resultValue = "";
	 	for(int i = firstPar+1; i < fullInput.length(); i++) {
	 		if(fullInput.charAt(i) == '(') {
	 			fullInput = parMethod(fullInput,i);
	 		}else if(fullInput.charAt(i) == ')') {
	 			resultValue = solve(fullInput.substring(firstPar+1,i),false);
	 			fullInput = frontParMethods(fullInput,resultValue,i,firstPar);
	 			break;
	 		}else if(i == fullInput.length()-1) {
	 			fullInput += ")";
	 			resultValue = solve(fullInput.substring(firstPar+1,fullInput.length()-1),false);
	 			fullInput = frontParMethods(fullInput,resultValue,fullInput.length()-1,firstPar);
	 			break;
	 		}
	 	}
	 	return fullInput;
	 }
	 //Method used to check if a Par has a letter operator
	 private String frontParMethods(String fullInput,String parResult, int i, int frontPar) {
		 System.out.println(fullInput);
		  int testValue;
		 if(frontPar > 0) {
	    	 switch(fullInput.charAt(frontPar-1)) {
	    	 case('f'):
	    		 if(containsValue(fullInput,"d→f",testValue = frontPar-3,frontPar)) {
				 	System.out.println(decimalParser(parResult));
				 	fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,decimalParser(parResult))  + fullInput.substring(i+1);	
				 }
	    	 break;
	    	 case('¹'): //sin⁻¹
	    		 //System.out.println("arcsin is found "+containsValue(fullInput,"sin⁻¹",testValue = parPos.getFirst()-5,parPos.getFirst()));
				 if(containsValue(fullInput,"sin⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.asin(Math.toRadians((new BigDecimal(parResult,mc).doubleValue())))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.asin((new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"cos⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.acos(Math.toRadians((new BigDecimal(parResult,mc).doubleValue())))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.acos((new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"tan⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.atan(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.atan(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.atan((new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.atan(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"csc⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.asin(1/(new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.asin(1/(new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"sec⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.acos(1/(new BigDecimal(parResult,mc).doubleValue()))).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.acos(1/(new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"cot⁻¹",testValue = frontPar-5,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(((new BigDecimal(piCon,mc).divide(BigDecimal.valueOf(2),mc)).subtract(BigDecimal.valueOf(Math.atan((new BigDecimal(parResult,mc).doubleValue()))))).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+(new BigDecimal(piCon,mc).divide(BigDecimal.valueOf(2),mc)).subtract(BigDecimalMath.atan(new BigDecimal(parResult),mc)).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.acos(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+((new BigDecimal(piCon,mc).divide(BigDecimal.valueOf(2),mc)).subtract(BigDecimalMath.atan(new BigDecimal(parResult),mc)))) + fullInput.substring(i+1);
				 		}
				 }
			 break;
			 case('n'):
				 System.out.println("n found");
				 if(containsValue(fullInput,"ln",frontPar-2,frontPar)) {
					 System.out.println(BigDecimal.valueOf(Math.log(Double.parseDouble(parResult))));
					 fullInput = fullInput.substring(0,frontPar-2) + mutiNeeded(frontPar-2,i+1,fullInput,""+(BigDecimalMath.log(new BigDecimal(parResult),mc).divide(BigDecimalMath.log(new BigDecimal(eCon),mc),mc))) + fullInput.substring(i+1);
				 
				 }else if(containsValue(fullInput,"arcsin",frontPar-6,frontPar)) {
					 System.out.println("arcsin Found");
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.asin(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.asin(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 }else if(containsValue(fullInput,"arctan",frontPar-6,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.atan(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.atan(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.atan(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.atan(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"tan",frontPar-3,frontPar)) {
					 if(cacMode == true) {
						    System.out.println(BigDecimal.valueOf(Math.tan(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.tan(new BigDecimal(parResult),mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)) + fullInput.substring(i+1);
				 			break;
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.tan(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.tan(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 			break;
				 		}
					 
				 } else if(containsValue(fullInput,"sin",frontPar-3,frontPar)) {
					 System.out.println("sin found");
					 if(cacMode == true) {
						    //System.out.println(BigDecimal.valueOf(Math.sin(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.sin(new BigDecimal(parResult),mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)) + fullInput.substring(i+1);
				 			//System.out.println("This full input after sin interp "+ fullInput);
				 			break;
					    }else {
				 			System.out.println(BigDecimal.valueOf(Math.sin(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.sin(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 			System.out.println("This full input after sin interp "+ fullInput);
				 			break;
				 		}
				 } 
			 break;
			 case('₀'): 
				 if (containsValue(fullInput,"log₁₀",frontPar-5,frontPar)) {
					 System.out.println(BigDecimal.valueOf(Math.log10(Double.parseDouble(parResult))));
					 fullInput = fullInput.substring(0,frontPar-5) + mutiNeeded(frontPar-5,i+1,fullInput,""+BigDecimalMath.log(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 }
			 break;
			 case('s'):
				  if(containsValue(fullInput,"arccos",frontPar-6,frontPar)) {
					 if(cacMode == true) {
				 			//System.out.println(BigDecimal.valueOf(Math.acos(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(parResult),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			//System.out.println(BigDecimal.valueOf(Math.acos(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 } else if(containsValue(fullInput,"cos",frontPar-3,frontPar)) {
					 if(cacMode == true) {
						    System.out.println(BigDecimal.valueOf(Math.cos(Math.toRadians(Double.parseDouble(parResult)))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.cos(new BigDecimal(parResult),mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)) + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.cos(Double.parseDouble(parResult))));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimalMath.cos(new BigDecimal(parResult),mc)) + fullInput.substring(i+1);
				 		}
				 }
			 break;
			 case('c'):
				 if(containsValue(fullInput,"arcsec",frontPar-6,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.acos(1/(new BigDecimal(parResult,mc).doubleValue()))).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.acos(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.acos(1/(new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc)) + fullInput.substring(i+1);
				 		}
				 }else if(containsValue(fullInput,"arccsc",frontPar-6,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println(BigDecimal.valueOf(Math.asin(1/(new BigDecimal(parResult,mc).doubleValue()))).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(Math.asin(1/(new BigDecimal(parResult,mc).doubleValue()))));
				 			fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+BigDecimalMath.asin(new BigDecimal(1).divide((new BigDecimal(parResult)),mc),mc)) + fullInput.substring(i+1);
				 		}
				 }else if(containsValue(fullInput,"sec",frontPar-3,frontPar)) {
					 if(cacMode == true) {
						    System.out.println(BigDecimal.valueOf(1).divide(new BigDecimal(Math.cos((new BigDecimal(parResult,mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)).doubleValue())),mc));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimal.valueOf(1).divide(BigDecimalMath.cos((new BigDecimal(parResult,mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)),mc),mc)) + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(1).divide(new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))),mc));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimal.valueOf(1).divide(new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))),mc)) + fullInput.substring(i+1);
				 		}
				 }else if(containsValue(fullInput,"csc",frontPar-3,frontPar)) {
					 if(cacMode == true) {
						    System.out.println(BigDecimal.valueOf(1).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)).doubleValue())),mc));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimal.valueOf(1).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).multiply(new BigDecimal(piCon,mc).divide(new BigDecimal(180,mc),mc),mc)).doubleValue())),mc)) + fullInput.substring(i+1);
				 		}else {
				 			System.out.println(BigDecimal.valueOf(1).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+BigDecimal.valueOf(1).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc)) + fullInput.substring(i+1);
				 		}
				 }
			 break;
			 case('t'):
				 if(containsValue(fullInput,"arccot",frontPar-6,frontPar)) {
					 if(cacMode == true) {
						 //System.out.println((new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc)).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
						 fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+(new BigDecimal(piCon,mc).divide(BigDecimal.valueOf(2),mc)).subtract(BigDecimalMath.atan(new BigDecimal(parResult),mc)).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
					 }else {
						 //System.out.println((new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc)));
						 fullInput = fullInput.substring(0,frontPar-6) + mutiNeeded(frontPar-6,i+1,fullInput,""+(new BigDecimal(piCon,mc).divide(BigDecimal.valueOf(2),mc)).subtract(BigDecimalMath.atan(new BigDecimal(parResult),mc))) + fullInput.substring(i+1);
					 }
				 }else if(containsValue(fullInput,"cot",frontPar-3,frontPar)) {
					 if(cacMode == true) {
				 			System.out.println((new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc)).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+(BigDecimalMath.cos((new BigDecimal(parResult,mc)),mc).divide(BigDecimalMath.sin((new BigDecimal(parResult,mc)),mc),mc)).multiply(new BigDecimal(180,mc).divide(new BigDecimal(piCon,mc),mc),mc))  + fullInput.substring(i+1);
				 		}else {
				 			System.out.println((new BigDecimal(Math.cos((new BigDecimal(parResult,mc).doubleValue()))).divide(new BigDecimal(Math.sin((new BigDecimal(parResult,mc).doubleValue()))),mc)));
				 			fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+(BigDecimalMath.cos((new BigDecimal(parResult,mc)),mc).divide(BigDecimalMath.sin((new BigDecimal(parResult,mc)),mc),mc))) + fullInput.substring(i+1);
				 		}
				 }
			 break;
			 case('d'):
				 if(containsValue(fullInput,"mod",frontPar-3,frontPar)) {
					 System.out.println("Mod found: 1st "+ backward(fullInput.substring(frontPar+1))+" 2nd " + backward(fullInput.substring(frontPar).substring(fullInput.substring(frontPar).indexOf(',')+1)));
					 fullInput = fullInput.substring(0,frontPar-3) + mutiNeeded(frontPar-3,i+1,fullInput,""+ new BigDecimal(backward(fullInput.substring(frontPar+1))).remainder(new BigDecimal(backward(fullInput.substring(frontPar).substring(fullInput.substring(frontPar).indexOf(',')+1))))) + fullInput.substring(i+1);
				 }
			 break;
			 default:
				 fullInput = fullInput.substring(0,frontPar) + mutiNeeded(frontPar,i+1,fullInput,""+new BigDecimal(parResult,mc)) + fullInput.substring(i+1);
	    	 }
	    	// System.out.println("ParMethod out "+fullInput);
	    	 return fullInput;
		 }else {
			 fullInput = fullInput.substring(0,frontPar) + mutiNeeded(frontPar,i+1,fullInput,""+new BigDecimal(parResult,mc)) + fullInput.substring(i+1);
			 System.out.println("ParMethod out "+fullInput);
			 return fullInput;
		 }
	 }
	 //Absolute value 
	 private String absoluteValue(String fullInput, int firstPar) {
		 String outputString = "";
		 for(int i = firstPar+1; i < fullInput.length(); i++) {
			 if(fullInput.charAt(i) == '|') {
				 outputString = fullInput.substring(0,firstPar) + new BigDecimal(solve(fullInput.substring(firstPar+1,i),false)).abs(mc)+ fullInput.substring(i+1);
				 System.out.println("Print for abs method returned string: " + outputString);
			 }
		 }
		 return outputString;
	 }
	 //uses hasNumValue to add * if needed
	 public String mutiNeeded(int front,int back, String fullString,String finalValue) {
		     //System.out.println("MutiNeeded ran, front is: " + fullString.substring(front,front+1)+" Back is: "+fullString.substring(back,back+1));
			 if (front > 0) {
				 if(isOperator(fullString.charAt(front-1),true)) {
					 
				 }else {
					 finalValue = "*"+finalValue;
				 }
			 
			 }
			if(back < fullString.length()) {
				if(isOperator(fullString.charAt(back),false)) {
					
				}else {
					finalValue = finalValue+"*";
				}
			}
			return finalValue;
		
	 }
	 //Method that is used to check letter values match parts of the string
	 private boolean containsValue(String fullInput, String checkValue, int parPos, int contain) {
		if(fullInput.length()-1 > contain-parPos && parPos >= 0) {
			 if(fullInput.substring(parPos,contain).equals(checkValue)) {
				System.out.println(fullInput.substring(parPos,contain)+"Equals"+ checkValue);
				return true;
			 }else {
				return false;
			 }
			 
		 } else {
			 return false;
		 }
	 }
	 //Method for basic factorial Math
	 private BigDecimal factorialMethod(String value) {
		 BigDecimal e = new BigDecimal(value,mc);
			for(int i = e.intValue()-1; i > 0; i--) {
				 e = e.multiply(BigDecimal.valueOf(i),mc);
			 }
			 return e;
		}
	 private String superScriptProcessing(String fullInput, int first) {
		 String base = forward(fullInput.substring(0, first));
		 String exponent = "";
		 String returnedString = "";
		 int sqrtPos = -1;
		 for (int i = first; i < fullInput.length(); i++) {
			 if (superscriptToNormal(fullInput.charAt(i)) != ',') {
				 exponent += superscriptToNormal(fullInput.charAt(i));
			 } else if (fullInput.charAt(i) == '√') {
				 sqrtPos = i;
				 break;
			 } else {
				 break;
			 }
		 }
		 if (sqrtPos != -1) {
			 returnedString = fullInput.substring(0, first - base.length()) + mutiNeeded(first - base.length(), sqrtPos + backward(fullInput.substring(sqrtPos + 1)).length() + 1, fullInput, "" + BigDecimalMath.pow(new BigDecimal(backward(fullInput.substring(sqrtPos + 1))),new BigDecimal(1).divide(new BigDecimal(exponent),mc), mc)) + fullInput.substring(sqrtPos + backward(fullInput.substring(sqrtPos + 1)).length() + 1);
		 } else {
			 returnedString = fullInput.substring(0, first - base.length()) + mutiNeeded(first - base.length(), first + exponent.length(), fullInput, "" + new BigDecimal(base).pow(Integer.parseInt(solve(exponent, false)), mc)) + fullInput.substring(first + exponent.length());
	 	 }
		 return returnedString;
	 }
	 private String decimalParser(String value) {
		 BigDecimal input = new BigDecimal(value,mc);
		 if(decimalToFraction(input.divide(new BigDecimal(piCon,mc),mc).doubleValue()).length() < decimalToFraction(input.doubleValue()).length()) {
			 String fraction = decimalToFraction(input.divide(new BigDecimal(piCon,mc),mc).doubleValue());
			 return fraction.substring(0,fraction.indexOf(',')) + "π" + fraction.substring(fraction.indexOf(','));
		 }else {
			 return decimalToFraction(input.doubleValue());
		 }
	 } 
	 private String decimalToFraction(double x){
		    if (x < 0){
		        return "-" + decimalToFraction(-x);
		    }
		    double tolerance = 1.0E-6;
		    double h1=1, h2=0, k1=0, k2=1, b = x;
		    do {
		        double a = Math.floor(b);
		        double aux = h1; 
		        h1 = a*h1+h2; 
		        h2 = aux;
		        aux = k1; 
		        k1 = a*k1+k2; 
		        k2 = aux;
		        b = 1/(b-a);
		    } while (Math.abs(x-h1/k1) > x*tolerance);

		    return (int) h1+",/"+(int)k1;
		}
	 public String inverse(String baseFunc, String inversePart, int variablePos) {
		String varSide;
		String shiftSide;
		ArrayList<String[]> pars = new ArrayList<String[]>();
		ArrayList<String[]> powers = new ArrayList<String[]>();
		ArrayList<String[]> tables = new ArrayList<String[]>();
		 for(int i = 0; i < inversePart.length();i++){
			 if(inversePart.charAt(i) == '{'){

			 }else if(inversePart.charAt(i) == '['){

			 }else if(inversePart.charAt(i) == 'ˋ'){

			 }
		 }
		 return baseFunc;
	 }
	 public int isVar(char entry, int charPos, String fullInput) {
		 if(entry == 'e') {
			 return 1;
		 }else if(entry == 'o'){
			 return 1;
		 }else if (entry == 's') {
			 if(containsValue(fullInput,"sin",charPos,charPos+3)) {
				 return 3;
			 }else if(containsValue(fullInput,"sec",charPos,charPos+3)) {
				 return 3;
			 }
		 }else if (entry == 'c') {
			 if(containsValue(fullInput,"cos",charPos,charPos+3)) {
				 return 3;
			 }else if(containsValue(fullInput,"csc",charPos,charPos+3)) {
				 return 3;
			 }else if(containsValue(fullInput,"cot",charPos,charPos+3)) {
				 return 3;
			 }
		 }else if (entry == 't') {
			 if(containsValue(fullInput,"tan",charPos,charPos+3)) {
				 return 3;
			 }
		 }else if(entry == 'a'){
			 if(containsValue(fullInput,"arcsin",charPos,charPos+6)) {
				 return 6;
			 }else if(containsValue(fullInput,"arccos",charPos,charPos+6)) {
				 return 6;
			 }else if(containsValue(fullInput,"arctan",charPos,charPos+6)) {
				 return 6;
			 }
		 }else if (entry == 'l') {
			 if(containsValue(fullInput,"ln",charPos,charPos+2)) {
				 return 2;
			 }else if(containsValue(fullInput,"log₁₀",charPos,charPos+5)) {
				 return 5;
			 }
		 }
		 return 0;
		 
	 }
	//Checks if a value has an operator 
		 public boolean isOperator(char i, boolean frontBack) {
			 if(frontBack == true) {
				 if (i == '+'|| i == '-'|| i == '/'|| i == '*'|| i == '×'|| i == '÷'|| i == '^'|| i == '%'|| i == '+'|| i == '-' || i == '('|| i == '√'|| i == '|') {
					 return true;
				 } else {
					 return false;
				 }
			 } else {
				 if (i == '+'|| i == '-'|| i == '/'|| i == '*'|| i == '×'|| i == '÷'|| i == '^'|| i == '%'|| i == '+'|| i == '-' || i == ')'|| i == '⁰'|| i == '¹'|| i == '²'|| i == '³'|| i == '⁴'|| i == '⁵'|| i == '⁶'|| i == '⁷'|| i == '⁸'|| i == '⁹'|| i == '|') {
					 return true;
				 } else {
					 return false;
				 }
			 }
		 }
		 private boolean isNum(String entry) {
			 for(int i = 0; i < entry.length(); i++) {
				 if(i == 0 && entry.charAt(0) == '-') {
					if(i == entry.length()-1) {
						return true;
					} 
				 }else if(((int) entry.charAt(i) > 47 && (int) entry.charAt(i) < 58) || entry.charAt(i) == '.' || i == 0 && entry.charAt(0) == '-'|| (entry.charAt(i) == '+' && entry.charAt(i-1) == 'E') ||(entry.charAt(i+1) == '+' && entry.charAt(i) == 'E')) {
					 if(i == entry.length()-1) {
						 return true;
					 }
				 }else {
					 return false;
				 }
			 }
			 return false;
		 }
		 private char superscriptToNormal(char check) {
			 switch(check) {
			 	case('⁰'):
			 		return '0';
			 	case('¹'):
			 		return '1';
			 	case('²'):
			 		return '2';
			 	case('³'):
			 		return '3';
			 	case('⁴'):
			 		return '4';
			 	case('⁵'):
			 		return '5';
			 	case('⁶'):
			 		return '6';
			 	case('⁷'):
			 		return '7';
			 	case('⁸'):
			 		return '8';
			 	case('⁹'):
			 		return '9';
			 	case('⁽'):
			 		return '(';
			 	case('⁾'):
			 		return ')';
			 	case('⁺'):
			 		return '+';
			 	case('⁻'):
			 		return '-';
			 	default:
			 		return ',';
			 }
		 }
}

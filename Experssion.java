package experssion;

import java.util.List;
import java.util.Stack;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class Experssion {
	
	private String experssion;  //算术表达式字符串
	
	private List<Word> word; //存储表达式词法分析所得的单词与该单词�?对应的种别编码的集合
	
	private int index;  //读取词法分析器的字符游标
	
	private int errorCode=0;  //表达式错误编�?
	
	private int sym;  //单词种别编码
	
	public Experssion(String expression) {
		this.experssion=expression;
		word=wordAnalysis(expression);
	}
	
	/**
	 * 词法分析器：用于扫描表达式并处理得到该表达式的词�?
	 * @param experssionStr  表达式字符串
	 * @return
	 */
	public static List<Word> wordAnalysis(String experssionStr){
		String expStrNoSpace=remove(experssionStr, ' '); //去除目标表达式中的空格字�?
		char[] exChar=expStrNoSpace.toCharArray();
		List<Word> word=new ArrayList<>();
		for(int i=0;i<exChar.length;i++) {
			//如果是操作符或�?�是括号
			if(exChar[i]=='+'||exChar[i]=='-'||exChar[i]=='*'||exChar[i]=='/'||exChar[i]=='('||exChar[i]==')') {
				StringBuilder sb=new StringBuilder();
				sb.append(exChar[i]);
				switch (exChar[i]) {
				case '+':
					word.add(new Word(sb.toString(),1));
					break;
				case '-':
					word.add(new Word(sb.toString(),2));
					break;
				case '*':
					word.add(new Word(sb.toString(), 3));
					break;
				case '/':
					word.add(new Word(sb.toString(), 4));
					break;
				case '(':
					word.add(new Word(sb.toString(), 6));
					break;
				case ')':
					word.add(new Word(sb.toString(), 7));
					break;
				default:
					break;
				}
			}else if(('a'<=exChar[i]&&exChar[i]<='z')||('A'<=exChar[i]&&exChar[i]<='Z')||exChar[i]=='_') {
				StringBuilder sb=new StringBuilder();
				while(('a'<=exChar[i]&&exChar[i]<='z')||('A'<=exChar[i]&&exChar[i]<='Z')||exChar[i]=='_'||('0'<=exChar[i]&&exChar[i]<='9')) {
					if(i==exChar.length-1) {
						sb.append(exChar[i]);
						break;
					}else {
						sb.append(exChar[i]);
						i++;
					}
				}
				if(exChar[i]=='.') {
					++i;
					if(('a'<=exChar[i]&&exChar[i]<='z')||('A'<=exChar[i]&&exChar[i]<='Z')||exChar[i]=='_'||('0'<=exChar[i]&&exChar[i]<='9')) {
						sb.append('.');
						while(('a'<=exChar[i]&&exChar[i]<='z')||('A'<=exChar[i]&&exChar[i]<='Z')||exChar[i]=='_'||('0'<=exChar[i]&&exChar[i]<='9')) {
							if(i==exChar.length-1) {
								sb.append(exChar[i]);
								break;
							}else {
								sb.append(exChar[i]);
								i++;
							}
						}
					}else {
						System.out.println("词法错误�?");
						System.exit(0);
					}
				}
				word.add(new Word(sb.toString(), 5));
				if(i==exChar.length-1) {
					switch (exChar[i]) {
					case '+':
						word.add(new Word("+",1));
						break;
					case '-':
						word.add(new Word("-",2));
						break;
					case '*':
						word.add(new Word("*", 3));
						break;
					case '/':
						word.add(new Word("/", 4));
						break;
					case '(':
						word.add(new Word("(", 6));
						break;
					case ')':
						word.add(new Word(")", 7));
						break;
					default:
						break;
					}
					break;
				}else {
					i--;
				}
				
			}else if('0'<=exChar[i]&&exChar[i]<='9') {
				StringBuilder sb=new StringBuilder();
				while('0'<=exChar[i]&&exChar[i]<='9') {
					if(i==exChar.length-1) {
						sb.append(exChar[i]);
						break;
					}else {
						sb.append(exChar[i]);
						i++;
					}
				}
				
				if(exChar[i]=='.') {
					++i;
					if(exChar[i]>='0'&&exChar[i]<='9') {
						sb.append('.');
						while(exChar[i]>='0'&&exChar[i]<='9') {
							if(i==exChar.length-1) {
								sb.append(exChar[i]);
								break;
							}else {
								sb.append(exChar[i]);
								i++;
							}
						}
					}else {
						System.out.println("词法错误�?");
						System.exit(0);
					}
				}
				word.add(new Word(sb.toString(), 5));
				if(i==exChar.length-1) {
					switch (exChar[i]) {
					case '+':
						word.add(new Word("+",1));
						break;
					case '-':
						word.add(new Word("-",2));
						break;
					case '*':
						word.add(new Word("*", 3));
						break;
					case '/':
						word.add(new Word("/", 4));
						break;
					case '(':
						word.add(new Word("(", 6));
						break;
					case ')':
						word.add(new Word(")", 7));
						break;
					default:
						break;
					}
					break;
				}else {
					i--;
				}
			}
			else {
				System.out.println("词法错误�?");
				System.exit(0);
			}
		}
		return word;
	}
	
	private static String remove(String srcStr,char ch) {
		StringBuilder sbBuilder=new StringBuilder();
		for(int i=0;i<srcStr.length();i++) {
			char currentChar=srcStr.charAt(i);
			if(currentChar!=ch) {
				sbBuilder.append(currentChar);
			}
		}
		return sbBuilder.toString();
	}
	
	public boolean isRightExpression() {
		errorCode=0;
		sym=0;
		index=0;
		//上述三条语句在进行表达式的合法�?�检测之前还原表达式的初始状态：原因表达式调用一次判断之后不会还原上述三个参�?
		next();
		E();
		return sym==0&&errorCode==0;
	}
	
	private void next() {
		if(index<word.size()) {
			sym=word.get(index).code;
			index++;
		}else {
			sym=0;
		}
	}
	
	/**
	 * 算数表达式的文法规则G(E)�?
	 *   E �? E+T | E-T | T 
	 *	 T �? T*F | T/F | F 
	 *	 F �? (E) | d
	 */
	private void E() {
		T();
		E1();
	}
	
	private void E1() {
		if(sym==1) {
			next();
			T();
			E1();
		}else if(sym==2){
			next();
			T();
			E1();
		}else if(sym!=7&&sym!=0) {
			errorCode=-1;
		}
	}
	
	private void T() {
		F();
		T1();
	}
	
	private void F() {
		if(sym==5) {
			next();
		}else if(sym==6) {
			next();
			E();
			if(sym == 7)
			{
				next();
			}
			else
			{
				errorCode = -1;
			}
		}else {
			errorCode=-1;
		}
	}
	
	private void T1() {
		if(sym==3) {
			next();
			F();
			T1();
		}else if(sym==4) {
			next();
			F();
			T1();
		}else if(sym!=0&&sym!=1&&sym!=2&&sym!=7) {
			errorCode=-1;
		}
	}
	
	public String getExperssion() {
		return experssion;
	}

	public void setExperssion(String experssion) {
		this.experssion = experssion;
	}
	
	/**
	 * 只提供get方法，返回Expression表达式对应的词法分析�?识别的结�?
	 * @return
	 */
	public List<Word> getWord() {
		return word;
	}
	
	/**
	 * 只提供get方法，用于返回表达式是否有误的编�?   0为没有错�? -1为表达式有误
	 */
	public int getErrorCode() {
		return errorCode;
	}
	
	/**
	 * 只提供get方法，用于返回表达式词法分析器中识别出的单词的种别编�?
	 * @return
	 */
	public int getSym() {
		return sym;
	}

	@Override
	public String toString() {
		return "Experssion [experssion=" + experssion + ", word=" + word + ", index=" + index + ", errorCode="
				+ errorCode + ", sym=" + sym + "]";
	}

	/*
	 * 根据种别编码获取运算符的优先�?
	 */
	private static int piror(int sym) {
		switch (sym) {
		case 1:
		case 2:
			return 1;  // +  - 对应的运算级别为 1
		case 3:
		case 4:
			return 2;  // *  / 对应的运算级别为 2
		default:
			return 0;  //  其余的运算级别为 0
		}
	}
	
	/*
	 * 根据种别编码判断是否是操作符
	 */
	private static boolean isOperation(int sym) {
		switch (sym)
		{
			case 1:  //  +
			case 2:  //  -
			case 3:  //  *
			case 4:  //  /
				return true;
			default:
				return false;
		}
	}
	
	private static boolean isNumber(int sym) {
		if(sym==5) { //单词种别编码�?5时表明该单词是一个操作数，返回true
			return true;
		}
		return false;
	}
	
	
	/**
	 * 中缀表达式转后缀表达�?
	 * @param midfix
	 * @return
	 */
	public static List<Word> getPostfix(Experssion expression){
		List<Word> midfix=expression.getWord();  //获取中缀表达式的经过词法分析器分析的结果�?
		List<Word> result=new ArrayList<>();   //存储后缀表达式结果的List集合
		Stack<Word> operationStack=new Stack<>();  //操作符栈
		
		if(expression.isRightExpression()) {//如果是正确的表达式则将执行将中缀表达式转换为后缀表达式的操作
			for(int i=0;i<midfix.size();i++) {
				Word currentWord=midfix.get(i);  //获取当前单词
				if(isOperation(currentWord.code)) {
					//如果操作符栈不为空且操作符栈的栈顶元素是操作符并且操作符栈顶的运算级别高于当前操作符的运算级�?
					while(!operationStack.isEmpty()&&isOperation(operationStack.peek().code)&&piror(operationStack.peek().code)>=piror(currentWord.code)) {
						result.add(operationStack.peek());  //将操作符栈顶元素添加到后�?表达式List�?
						operationStack.pop();  //操作符栈顶元素出�?
					}
					operationStack.push(currentWord);  //当前操作符入�?
				}else if(currentWord.code.equals(6)) {  //如果当前单词是左括号 无条件入�?
					operationStack.push(currentWord);
				}else if(currentWord.code.equals(7)) {//当前单词是右括号 将操作符栈中的左括号以前的操作符出栈
					while(!operationStack.peek().code.equals(6)) { //如果栈顶操作符不是左括号
						result.add(operationStack.peek()); //栈顶操作符加入后�?表达式List�?
						operationStack.pop();  //栈顶操作符出�?
					}
					operationStack.pop(); //当前操作符入�?
				}else {
					result.add(currentWord); //当前单词不是操作符，即数字直接加入到后缀表达式的List�?
				}
			}
			//中缀表达式链表元素遍历完毕，�?要将栈中元素依次出栈加入到后�?表达式List�?
			while(!operationStack.isEmpty()) {
				result.add(operationStack.peek());
				operationStack.pop();
			}
		}else {//不是正确的表达式，给出提示信�?
			System.out.println("表达式有误！");
		}
		return result;
	}
	
	/**
	 * 计算表达式的运算结果
	 * @param experssion
	 * @return
	 */
	public static BigDecimal expCalculate(Experssion experssion) {
		List<Word> postfix=getPostfix(experssion);  //获取表达式的后缀表达�?
		Stack<BigDecimal> result=new Stack<>();  //操作数栈
		if(postfix!=null&&postfix.size()>0) {
			for(int i=0;i<postfix.size();i++) {
				Word currentWord=postfix.get(i);
				if(isNumber(currentWord.code)) {
					result.push(new BigDecimal(currentWord.word));
				}else if(isOperation(currentWord.code)) {
					BigDecimal first=result.peek();
					result.pop();
					BigDecimal second=result.peek();
					result.pop();
					switch (currentWord.code) {
					case 1:
						result.push(second.add(first));
						break;
					case 2:
						result.push(second.subtract(first));
						break;
					case 3:
						result.push(second.multiply(first));
						break;
					case 4:
						result.push(second.divide(first,4,BigDecimal.ROUND_HALF_UP));
						break;
					default:
						break;
					}
				}
			}
		}
		return result.peek();
	}
	
	public static void main(String[] args) {
		
		Experssion e1=new Experssion("a_qq.b+5b+6cu+f");
		Experssion e2=new Experssion("((12*(2.50-1.05"+")"+"-10)+90)");
		Experssion e3=new Experssion("3 * 5 / ("+" 12 + 11 "+")");
		
		List<Word> words1=Experssion.wordAnalysis(e1.getExperssion());
		//e1.setWord(words1);
		System.out.println(words1);
		System.out.println(e1.isRightExpression());
		System.out.println(e1);
		
		List<Word> words2=Experssion.wordAnalysis(e2.getExperssion());
		//e2.setWord(words2);
		System.out.println(words2);
		System.out.println(e2.isRightExpression());
		System.out.println(expCalculate(e2));
		System.out.println(e2);
		
		List<Word> words3=Experssion.wordAnalysis(e3.getExperssion());
		//e3.setWord(words3);
		System.out.println(words3);
		System.out.println(e3);
		System.out.println(e3.isRightExpression());
		System.out.println(expCalculate(e3));
		
		
		
		Experssion e4=new Experssion("tc_zd_plan.xztdmj+tc_zd_plan.cltdmj");
		System.out.println(e4);
		System.out.println(e4.getWord());
		//System.out.println(expCalculate(e4));
		System.out.println(e4.isRightExpression());
		System.out.println(e4);
		
		//System.out.println(expCalculate(getPostfix(words4)));
		
		/*
		LocalDate date=LocalDate.now();
		System.out.println(date.getYear());
		System.out.println(date.getMonthValue());
		System.out.println(date.getDayOfMonth());
		*/
	} 
}

class Word{
	
	String word;  //单词部分
	
	Integer code;  //单词种别编码

	@Override
	public String toString() {
		return "("+word+","+String.valueOf(code)+")";
	}
	
	public Word(String word,Integer code) {
		this.word=word;
		this.code=code;
	}
}

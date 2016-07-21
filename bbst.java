import java.util.*;
import java.io.*;
import java.lang.*;

public class bbst {
	private Node root;
	private static final boolean RED   = false;
	private static final boolean BLACK = true;
	
	bbst(){}
	
	
/*
* Nested Class:Node used to contain the information
*/
	class Node{
		/*Fields*/
		int id;
		int count;
		boolean color=BLACK;
		Node parent;
		Node left=null,right=null;
		Node(){}
		Node(int id, int count){
			this.id=id;
			this.count=count;
		}
		Node(int id,int count, Node parent){
			this.id=id;
			this.count=count;
			this.parent=parent;
		}
		public int getid(){ return id;}
		public int getcount(){return count;}
		public void increase(int m){this.count+=m;}
		public void decreaes(int m){this.count-=m;}
		public String toString(){
			return id+" "+count;
		}
	}
	
/*Define utility functions to manipulate node*/
	private static boolean colorOf(Node a){
		return (a==null)?BLACK:a.color;
	}
	
	private static Node parentOf(Node a){
		return (a==null)?null:a.parent;
	}
	
	private static void setColor(Node a, boolean c){
		if(a!=null) a.color=c;
	}
	
	private static Node leftOf(Node a){
		return (a==null)?null:a.left;
	}
	
	private static Node rightOf(Node a){
		return (a==null)?null:a.right;
	}
	
	private void rotateLeft(Node a){
		if(a!=null){
			Node r=a.right;
			a.right=r.left;
			if(r.left!=null) r.left.parent=a;
			r.parent=a.parent;
			if(a.parent==null) root=r;
			else if(a.parent.left==a) a.parent.left=r;
			     else 				  a.parent.right=r;
			r.left=a;
			a.parent=r;
		}
	}
	
	private void rotateRight(Node a){
		if(a!=null){
			Node l=a.left;
			a.left=l.right;
			if(l.right!=null) l.right.parent=a;
			l.parent=a.parent;
			if(a.parent==null) root=l;
			else	if(a.parent.right==a) a.parent.right=l;
					else				  a.parent.left=l;
			l.right=a;
			a.parent=l;
		}
	}
	
	final Node getNode(int id){
		Node p=root;
		while(p!=null){
			if(p.id==id) return p;
			if(p.id>id) p=p.left;
			else		p=p.right;
		}
		return null;
	}
	
	final Node getNextNode(int id){
		Node p=root;
		while(p!=null){
			int cmp=id-p.id;
			if(cmp<0)	if(p.left!=null) p=p.left;
						else			return p;
			else{
				if(p.right!=null) p=p.right;
				else{
					Node parent=p.parent;
					Node ch=p;
					while(parent!=null&&ch==parent.right){ ch=parent; parent=parent.parent;}
					return parent;
				}
			}
		}
		return null;
	}
	
	final Node getPreviousNode(int id){
		Node p=root;
		while(p!=null){
			int cmp=id-p.id;
			if(cmp>0){
				if(p.right!=null) p=p.right;
				else			  return p;
			}else{
				if(p.left!=null) p=p.left;
				else{
					Node parent=p.parent;
					Node ch=p;
					while(parent!=null && ch==parent.left){ ch=parent; parent=parent.parent;}
					return parent;
				}
			}
		}
		return null;
	}
	//increase count of id with m or insert (id m) if not found
	public Node increaseBy(int id, int plus){
		Node t=root, parent;
		if(t==null) {root=new Node(id,plus,null); return root;}
		int cmp=0;
		do{
			parent=t;
			cmp=id-t.id;
			if(cmp<0) t=t.left;
			else	if(cmp>0) t=t.right;
					else{
						t.increase(plus);
						return t;
					}
		}while(t!=null);
		Node e=new Node(id,plus,parent);
		if(cmp<0) parent.left=e;
		else	  parent.right=e;
		fixAfterInsertion(e);
		return e;
	}
	
/* Return the successor Node of the specified Node or null if there is no such node*/
	static Node successor(Node t){
		if(t==null)	return null;
		if(t.right!=null){
			Node p=t.right;
			while(p.left!=null)	p=p.left;
			return p;
		}else{
			Node p=t.parent, ch=t;
			while(p!=null && ch==p.right){ ch=p; p=p.parent;}
			return p;
		}
	}
	
	
	
	
	private void fixAfterInsertion(Node x){
		x.color=RED;
		while(x!=null && x!=root && x.parent.color==RED){
			if(parentOf(x)==leftOf(parentOf(parentOf(x)))){
				Node y=rightOf(parentOf(parentOf(x)));
				if(colorOf(y)==RED){
					setColor(parentOf(x),BLACK);
					setColor(y,BLACK);
					setColor(parentOf(parentOf(x)),RED);
					x=parentOf(parentOf(x));
				}else{
					if(x==rightOf(parentOf(x))){ x=parentOf(x); rotateLeft(x);}
					setColor(parentOf(x),BLACK);
					setColor(parentOf(parentOf(x)),RED);
					rotateRight(parentOf(parentOf(x)));
					}
				}
			else {
				Node y=leftOf(parentOf(parentOf(x)));
				if(colorOf(y)==RED){
					setColor(parentOf(x),BLACK);
					setColor(y,BLACK);
					setColor(parentOf(parentOf(x)),RED);
					x=parentOf(parentOf(x));
				}else{
					if(x==leftOf(parentOf(x))){ x=parentOf(x);rotateRight(x);}
					setColor(parentOf(x),BLACK);
					setColor(parentOf(parentOf(x)),RED);
					rotateLeft(parentOf(parentOf(x)));
				}
			}
		}
		root.color=BLACK;
	}
	
	private void deleteNode(Node p){
		//if p is a two degree node
		if(p.left!=null && p.right!=null){
			Node s=successor(p);
			p.id=s.id;
			p.count=s.count;
			p=s;
		}
		//Start fixup at replacement node
		Node replacement=(p.left!=null)?p.left:p.right;
		
		if(replacement!=null){
			replacement.parent=p.parent;
			if(p.parent==null) root=replacement;
			else	if(p==p.parent.left) p.parent.left=replacement;
					else				p.parent.right=replacement;
			p.left=p.right=p.parent=null;
			
			//fix replacement
			if(p.color==BLACK) fixAfterDeletion(replacement);
		}else	if(p.parent==null) root=null;//p is the only node we have
				else{
					if(p.color==BLACK) fixAfterDeletion(p);
					if(p.parent!=null){
						if(p==p.parent.left)	p.parent.left=null;
						else if(p==p.parent.right)	p.parent.right=null;
						p.parent=null;
					}
				}
	}
	
/* Fix the red black tree after delete one node*/
	private void fixAfterDeletion(Node x){
		while(x!=root && colorOf(x)==BLACK){
			if(x==leftOf(parentOf(x))){
				Node sib=rightOf(parentOf(x));
				
				if(colorOf(sib)==RED){
					setColor(sib,BLACK);
					setColor(parentOf(x), RED);
					rotateLeft(parentOf(x));
					sib=rightOf(parentOf(x));
				}
				
				if(colorOf(leftOf(sib))==BLACK && colorOf(rightOf(sib))==BLACK) { setColor(sib, RED); x=parentOf(x);}
				else{
					if(colorOf(rightOf(sib)) == BLACK){
						setColor(leftOf(sib),BLACK);
						setColor(sib, RED);
						rotateRight(sib);
						sib=rightOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(rightOf(sib), BLACK);
					rotateLeft(parentOf(x));
					x=root;
				}
			}else{//x=leftOf(parentOf(x))
				Node sib=leftOf(parentOf(x));
				
				if(colorOf(sib)==RED){
					setColor(sib,BLACK);
					setColor(parentOf(x), RED);
					rotateRight(parentOf(x));
					sib=leftOf(parentOf(x));
				}
				
				if(colorOf(rightOf(sib))==BLACK && colorOf(leftOf(sib))==BLACK) { setColor(sib, RED); x=parentOf(x);}
				else{
					if(colorOf(leftOf(sib)) == BLACK){
						setColor(rightOf(sib),BLACK);
						setColor(sib, RED);
						rotateLeft(sib);
						sib=leftOf(parentOf(x));
					}
					setColor(sib, colorOf(parentOf(x)));
					setColor(parentOf(x), BLACK);
					setColor(leftOf(sib), BLACK);
					rotateRight(parentOf(x));
					x=root;
				}
				
			}
		}
		setColor(x, BLACK);
	}
	
/* Interface open to command*/
	public void increase(int id, int m){
		Node e=increaseBy(id,m);
		System.out.println(e.count);
	}
	
	public void reduce(int id, int m){
		Node e=getNode(id);
		if(e==null) System.out.println(0);
		else if(e.count<=m) { deleteNode(e); System.out.println(0);}
			 else			{ e.decreaes(m); System.out.println(e.count);}
	}
	
	public void count(int id){
		Node e=getNode(id);
		int result=0;
		if(e!=null) result+=e.count;
		System.out.println(result);
	}
	
	public void inrange(int id1, int id2){
		int sum=0;
		Node start=getNode(id1);
		if(start==null) start=getNextNode(id1);
		while(start!=null&&start.id<=id2){
			sum+=start.count;
			start=successor(start);
		}
		System.out.println(sum);
	}
	
	public void next(int id){
		Node e=getNextNode(id);
		if(e==null) System.out.println("0 0");
		else 		System.out.println(e.id+" "+e.count);
	}
	
	public void previous(int id){
		Node e=getPreviousNode(id);
		if(e==null) System.out.println("0 0");
		else 		System.out.println(e.id+" "+e.count);
	}
	
/*Initialize help function*/
	Node buildfrom(int[][] arr, int start, int end){
		if(end<start) return null;
		if(start==end) return new Node(arr[start][0],arr[start][1],null);
		int len=end-start+1;
		int rootdiff = len%2==0?(len/2-1):(len/2);
		Node root=new Node(arr[start+rootdiff][0],arr[start+rootdiff][1],null);
		Node left=buildfrom(arr,start,start+rootdiff-1);
		Node right=buildfrom(arr,start+rootdiff+1,end);
		if(left!=null){
			left.parent=root;
			root.left=left;
		}
		if(right!=null){
			right.parent=root;
			root.right=right;
		}
		return root;
	}
	
	
/*Main function*/
	public static void main (String[] args)throws IOException{
/*Read in initialization data file and handle unexpected error*/
		if(args.length<1){
			System.out.println("Miss initialization data!");
			return;}
		String input_path=args[0];
		Scanner data=null;
		int[][] idcount=null;
		bbst rbt=new bbst();
		try{//Input file should be in the right format
			data=new Scanner(new File(input_path));
			int line=0;
			if(data.hasNext()){
				line=data.nextInt();
				idcount=new int[line][2];
				for(int i=0;i<line;i++){
				idcount[i][0]=data.nextInt();
				idcount[i][1]=data.nextInt();
				}
			}
		}catch(IOException e){
			System.out.println("Fail to read data from "+input_path);
			return;
		}
		finally{
			if(data!=null) data.close();
		}
/* Build Red Black Tree from sorted key array*/
		if(idcount!=null&&idcount.length!=0){
			int len=idcount.length;
			int rootindex=len%2==0?(len/2-1):(len/2);
			rbt.root=rbt.new Node(idcount[rootindex][0],idcount[rootindex][1],null);
			bbst.Node leftside=rbt.buildfrom(idcount,0,rootindex-1);
			bbst.Node rightside=rbt.buildfrom(idcount,rootindex+1,len-1);
			if(leftside!=null) {
				leftside.parent=rbt.root;
				rbt.root.left=leftside;
			}
			if(rightside!=null){
				rightside.parent=rbt.root;
				rbt.root.right=rightside;
			}
			if(len%2!=0 && rootindex-1>=0){
					rbt.getNode(idcount[rootindex-1][0]).color=RED;
			}
			rbt.getNode(idcount[len-1][0]).color=RED;
		}

		
		
/////////////////////////////////////////////////////////////////////////
		String menu="Welcome to the console, Please selecte your operation:\n"+
					String.format("%20s    :    %-40s\n","(increase id m)","increase the count of id by m")+
					String.format("%20s    :    %-40s\n","(reduce id m)","decrease the count of id by m")+
					String.format("%20s    :    %-40s\n","(count id)","print the count of id")+
					String.format("%20s    :    %-40s\n","(inrange id1 id2)","print the total count for IDs between ID1 and ID2 inclusively")+
					String.format("%20s    :    %-40s\n","(next id)","print the id and the count of the event with the lowest ID that's greater than the ID")+
					String.format("%20s    :    %-40s\n","(previous id)","print the id and the count of the event with the greatest id that is less than the ID")+
					String.format("%20s    :    %-40s\n","(quit)","to quit this menu");
			/*System monitor print, Uncomment for console input*/
			//System.out.println(menu);
			Scanner in=new Scanner(System.in);
			while(in.hasNextLine()){
				String line=in.nextLine();
				Scanner in_line=new Scanner(line);
				//If there is no command on this line, print "Wrong operation"
				if(!in_line.hasNext()) System.out.println("Wrong operation!");
				else{
				String operation=in_line.next();
				switch(operation){
				//for all the correct command, there should be also correct number of operand following each operator
				case "increase":{
					rbt.increase(in_line.nextInt(), in_line.nextInt());
					break;}
				case "reduce":{
					rbt.reduce(in_line.nextInt(), in_line.nextInt());
					break;}
				case "count" :{
					rbt.count(in_line.nextInt());
					break;}
				case "inrange" :{
					rbt.inrange(in_line.nextInt(), in_line.nextInt());
					break;}
				case "next" :{ 
					rbt.next(in_line.nextInt());
					break;}
				case "previous" :{ 
					rbt.previous(in_line.nextInt());
					break;}
				case "quit" :{
					in_line.close(); 
					if(in!=null) in.close(); 
					return;}
				//If the command is beyond operations supported by this program, print "Wrong operation"
				default:System.out.println("Wrong opration");
				}
				in_line.close();
			}}
			if(in!=null) in.close();
		}
		
	}

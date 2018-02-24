package col106.a3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.lang.reflect.Array;
import java.lang.*;

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {

	
	private static int t;
	private Node root;
	private int height;
	//========= number of key-value pair int btree===========
	private int num=0; 
	//  Class< ? extends structure>cls;
	private static final class Node {
		private int membernum;
		private int childnum;
		private Node parent = null;
		
		@SuppressWarnings("unchecked")
		private structure[] members = new structure[2*t];
		private Node[] children = new Node[2*t+1];
		//===== constructer ==========
		public Node(int ch)
		{
			this.childnum = ch;
		}
		private Comparator<Node> comparator = new Comparator<Node>() {
			public int compare(Node n1,Node n2)
			{
				return n1.members[0].key.compareTo(n2.members[0].key);
			}
		};
		private Comparator<structure> comparator1 = new Comparator<structure>() {
			public int compare(structure s1,structure s2)
			{
				return s1.key.compareTo(s2.key);
			}
		};
		public void add(structure s,int index)
		{
			if(index >=membernum)
				this.members[this.membernum] = s;
			else
			{	for(int i=0;i<membernum - index;i++)
				{
					this.members[membernum - i] = this.members[membernum - i- 1];
				}
				this.members[index] = s;
			
			}
			//=== for debugging==========
			/*
			if(this.parent==null)
			{
				System.out.println(s.key+"   "+this.membernum);
			}
			*/
			membernum++;
			//Arrays.sort(members,0,this.membernum,comparator1);
		}
		public void addchild(Node n,int index)
		{
			n.parent = this;
			if(index >= childnum)
			{
			//System.out.println(childnum);
				this.children[childnum] = n;
			}
			else
			{
				for(int i=0;i<childnum-index;i++)
				{
					this.children[childnum-i] = this.children[childnum-i-1];
				}
				this.children[index] = n;
			}
			childnum++;
			//Arrays.sort(children,0,childnum,comparator);
		}
		public final void removekey(Comparable key)
		{
			int pr=0;
			for(int i=0;i<membernum;i++)
			{
				if(pr==1)
				{
					members[i-1]=members[i];
				}
				else if(members[i].key.equals(key))
				{
					pr=1;
				}
			}
			if(pr==1)
			{
				membernum--;
				members[membernum]=null;
			}
		}
		public void removechild(Node child)
		{
			int pr=0;
			/*for(int i=0;i<child.membernum;i++)
			{
				System.out.println(child.members[i].key+" , ");
			}*/
			//System.out.println(childnum);
			
			//System.out.println("in remove");
			for(int i=0;i<childnum;i++)
			{
				if(pr==1)
				{
					children[i-1] = children[i];
				}
				if(children[i].equals(child)&& pr==0)
				{
					pr=1;
				//	System.out.println("hi guys  "+i+"\n");
					//this.children[i] = null;
					//break;
				}
				
			}
			if(pr==1)
			{
				childnum--;
			children[childnum]=null;
			
			}
		//	Arrays.sort(children,0,childnum,comparator);
			//System.out.println(childnum);
		//	this.children[index] = left;
		}
	}
	//====== structure class ============
	 private static class structure{
		 private Comparable key;
		 private final Object obj;
		 //private Node chi;
		public structure (Comparable key,Object obj) {
			this.key = key;
			this.obj = obj;
		//	this.chi = child;
		}
		
	}
    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
	if(b%2==1)
	{
	//	System.out.println("will you thorw it");
		throw new bNotEvenException();    	
	}
	//System.out.println("no i will not");
	t = b/2;
    	root = new Node(0); 
    	// throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean isEmpty() {
       return size()==0;
    	// throw new RuntimeException("Not Implemented");
    }

    @Override
    public int size() {
        return num;
    	//throw new RuntimeException("Not Implemented");
    }

    @Override
    public int height() {
        return height;
    	//throw new RuntimeException("Not Implemented");
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        List<Value> val = new ArrayList<Value>();
       // System.out.println(val +"   this is where is begins");
        val = sea(root,key,val);
	if(val.size() == 0)
		throw new IllegalKeyException();
  //      System.out.println(val +"   this is where it ends");
        
        return val;
    	
    }
    private List<Value> sea(Node n,Comparable<Key> key,List<Value> val)
    {
    	
    	for(int i=0;i<n.membernum;i++)
    	{
    	//	System.out.println(n.members[i].key+"  "+n.membernum);
    		if(n.childnum >0 && (!greater(key,n.members[i].key)))
    		{
    		//	System.out.println("entering unkwon terotory");
    			val = sea(n.children[i],key,val);
    		}
    		if(equal(n.members[i].key,key))
    		{
    			//System.out.println(n.members[i].obj+" yup");
    			val.add((Value)n.members[i].obj);
    		}
    		if(less(key,n.members[i].key))
    		{
    		//	System.out.println("fun ends at" + n.members[i].key);
    			return val;
    		}
    	}
    	if(n.childnum > 0)
    	val = sea(n.children[n.childnum-1],key,val);
    			
    	return val;
    }
   
   // @Override
    public String toString()
    {
    	StringBuilder builder = new StringBuilder();
    	
    	builder = btreestring(root,builder);
    //	builder.append('\n');
		return builder.toString();
    //	string s;
    	
   // 	return s;
    }
    private StringBuilder btreestring(Node n,StringBuilder build)
    {
    	build.append("[");
    	for(int i=0;i<n.membernum;i++)
    	{
    		if(n.childnum>0)
    		{
    			build = btreestring(n.children[i],build);
    			build.append(", ");
    		}
    		build.append(n.members[i].key+"="+n.members[i].obj);
    		if(i != n.membernum-1)
    		build.append(", ");
    		
    	}
    	if(n.childnum>0)
    	{
    		build.append(", ");
    		build = btreestring(n.children[n.childnum-1],build);
    	}
    	
    	
    	build.append("]");
    	return build;
    }
    
    @Override
    public void insert(Key key, Value val) {
        //throw new RuntimeException("Not Implemented");
    	Node n= root;
    	int ht = height;
    	structure s = new structure(key,val);
    	while(n!=null)
    	{
    		// while only there is root or at leaf
    		if(n.childnum==0)
    		{
    			if(n.membernum<2*t)
    			{
				int var = n.membernum;
				for(int i=0;i<n.membernum;i++)
				{
					if(less(key,n.members[i].key))
					{	
						var = i ;
						break;
					}
				}
    				n.add(s,var);
    				num++;
    			}
    			if(n.membernum <2*t)
    				break;
    			split(n);
    			break;

    		}
    		//====== need to go downwards to insert =============
    		else
    		{
    			if(less(key,n.members[0].key)|| equal(key,n.members[0].key))
    				n = n.children[0];
    			else if(greater(key,n.members[n.membernum-1].key))
    				n= n.children[n.childnum-1];
    			//== check in between ==============
    			else {
    				for(int i=1;i<n.membernum;i++)
    				{
    					if(less(key,n.members[i].key)||equal(key,n.members[i].key))
    					{
    						n = n.children[i];
    						break;
    					}
    				}
    			}
    			
    		}
    	}
    	
    }
    
    private void split(Node n) {
    	Node node = n;
    	
    	// ==== left Node constructer===========
    	Node left  = new Node(0);
    	for( int i=0;i < n.membernum/2;i++ )
    	{
    		structure s = new structure(n.members[i].key,n.members[i].obj);
    		left.add(s,i);
    	}
    	if(node.childnum > 0)
    	{
    		for(int i=0;i<=n.membernum/2;i++)
    		{
    			Node child = node.children[i];
    			left.addchild(child,i);
    		}
    	}
    	
    	// ========= Right Node constructer =============
    	Node right = new Node(0);
    	for( int i=node.membernum/2+1;i<node.membernum;i++)
    	{
    		structure s = new structure(n.members[i].key,n.members[i].obj);
    		right.add(s,i-node.membernum/2-1);
    	}
    	if(node.childnum > 0)
    	{
    		for(int i=node.membernum/2+1;i<n.childnum;i++)
    		{
    			Node child = node.children[i];
    			right.addchild(child,i-node.membernum/2-1);
    		}
    	}
    	
    	// ===  if parent is null then root is breaked ======
    	if(node.parent == null)
    	{
    		height++;
    		Node rootnew = new Node(0);
    		structure s = new structure(node.members[node.membernum/2].key,node.members[node.membernum/2].obj);
    		rootnew.add(s,0);
    		rootnew.parent = null;
    		
    		// updateing root
    		root = rootnew;
    		
    		//==== adding children to new root =================
    		root.addchild(left,0);
    		root.addchild(right,1);
    		
    	}
    	else
    	{
    		Node parent = node.parent;
		int index =0;
		for(int i=0;i<parent.childnum;i++)
		{
			if(node.equals(parent.children[i]))
				index = i;
		}
    		
    		structure s = new structure(node.members[node.membernum/2].key,node.members[node.membernum/2].obj);
    		parent.add(s,index);
    		// ===========================================================
    		//========== need to remove old node too ======================
    		//============================================================
    		parent.removechild(node);
    		//System.out.println(toString());
    		parent.addchild(left,index);
    		parent.addchild(right,index+1);
    		if(parent.membernum >= 2*t)
    			split(parent);
    	}
    	
    }
    
    /*
    private Node fit(Node n,Key key,Value val,int ht)
    {
    	structure s = new structure(key,val);
    	// at leaf
    	if(ht == 0)
    	{
    		for(int j=0;j<n.childnum;j++)
    		{
    			if(less(key,n.children[j].key))
    			{
    				break;
    			}
    		}
    	}
    	else
    	{
    		for(int i=0;i<n.childnum;i++)
    		{
    			if(less(key,n.children[i+1].key))
    			{
    				Node u = fir(n.children[i++],key,val,ht-1);
    				if(u == null)
    					return null;
    				s.key = u.children[0].key;
    				s.next = u;
    				break;
    			}
    		}
    	}
    	
    }
    */

    @Override
    public void delete(Key key) throws IllegalKeyException {
    	int count =0;
    	Node n;
	//father = root;
    	for(int i=0;;i++)
    	{
    		count++;
//		System.out.println(this.toString());
//		System.out.println(this.toString());
    		 n = find(root,key);
    		if(n == null)
    			break;
    		else
    		{
    			int var=0;
				for(int  i1=0;i1<n.membernum;i1++)
				{
					if(equal(key,n.members[i1].key))
					{
						var = i1;
					}
				}
    			
    			
    			//n.removekey(key);
    			
    			//========= if leaf==========
    			if(n.childnum == 0)
    			{
				n.removekey(key);
    				if(n.parent !=null && n.membernum < t-1)
    				{
    					merge(n);
    				}
    				else if(n.parent == null &&n.membernum == 0)
    				{
    					// if no keys then root is null
    					if(num == 0)
    						root = null;
    				
    				}
    			}
    			else
    			{
    				
    				structure predecessor = getlarge(n.children[var],n,var);
    				
    			//	n.add(predecessor,var);
    				
    			}
    			num --;
    			
    			
    		//	System.out.println("doing well buddy");
    		}
    		
    	}
//=========================================================================================
//================ need to revert back =====================================================
//==========================================================================================
    
    		if(count == 1)
    			throw new IllegalKeyException();
  
    	
    	//throw new RuntimeException("Not Implemented");
    }
    private structure getlarge(Node n,Node par,int var)
    {
    	if(n.childnum == 0)
    	{
    		structure s = n.members[n.membernum-1];
    		n.removekey(s.key);
		par.members[var] = s;
    		if(n.membernum < t-1)
    		{
//			System.out.println("so i did merge");
			merge(n);
    			
		}
		return s;
    	}
    	else
    	{
//		if(n.membernum == t-1 && !n.equals(root))
//			merge(n);
    		return getlarge(n.children[n.childnum-1],par,var);
    	}
    }
    private void merge(Node n)
    {
//	System.out.println("inside mergee with"+ n.membernum +"  guys  "+n.parent.membernum);
    	Node parent = n.parent;
    	int var =0;
    	for(int i=0;i<parent.childnum;i++)
    	{
    		if(n.equals(parent.children[i]))
    		{
    			var = i;
    			break;
    		}
    	}
    	
    	Node right = null;
    	if(var+1<parent.childnum)
    	{
    		right = parent.children[var+1];
    	}
    	
    	// borrow from right neighbor
    	if(right!=null && right.membernum > t-1)
    	{
	//	System.out.println("making a right borrow");
    		n.add(parent.members[var],n.membernum);
    		parent.removekey(parent.members[var].key);
    		parent.add(right.members[0],var);
    		right.removekey(right.members[0].key);
    		if(right.childnum >0)
    		{
    			n.addchild(right.children[0],n.childnum);
    			right.removechild(right.children[0]);
    			
    		}
    	}
    	else
    	{
    		Node left = null;
    		if(var-1>=0)
    		{
    			left = parent.children[var-1];
    		}
    		if(left!=null && left.membernum > t-1)
    		{
		//	System.out.println("left borrow");
    			n.add(parent.members[var-1],0);
        		parent.removekey(parent.members[var-1].key);
        		parent.add(left.members[left.membernum-1],var-1);
        		left.removekey(left.members[left.membernum-1].key);
        		if(left.childnum >0)
        		{
        			n.addchild(left.children[left.childnum-1],0);
        			left.removechild(left.children[left.childnum-1]);
        			
        		}
    		}
    		// if borrow didn't work
    		else if(right!=null && parent.membernum > 0)
    		{
		//	System.out.println("got till here");
    			n.add(parent.members[var],n.membernum);
    			parent.removekey(parent.members[var].key);
    			for(int i=0;i<right.membernum;i++)
    			{
    				n.add(right.members[i],n.membernum);
    			}
    			for(int i=0;i<right.childnum;i++)
    			{
    				n.addchild(right.children[i],n.childnum);
    			}
    			parent.removechild(right);
    			
    			if(parent.parent !=null && parent.membernum < t-1)
    			{
    				merge(parent);
    			}
    			else if(parent.membernum == 0)
    			{
    				// root shift one step down
    				n.parent = null;
    				root = n;
    				height--;
    			}
    		}
    		else if(left !=null && parent.membernum >0)
    		{
    			// merge with left
			//System.out.println("left merger");
    			n.add(parent.members[var-1],0);
    			parent.removekey(parent.members[var-1].key);
    			
    			for(int i=left.membernum-1;i>=0;i--)
    			{
    				n.add(left.members[i],0);
    			}
    			for(int i=left.childnum-1;i>=0;i--)
    			{
    				n.addchild(left.children[i],0);
    			}
    			parent.removechild(left);
    			if(parent.parent !=null && parent.membernum < t-1)
    			{
    				merge(parent);
    			}
    			else if(parent.membernum == 0)
    			{
    				// root shift one step down
    				n.parent = null;
    				root = n;
    				height--;
    			}
    			
    		}
    			
    	}
    	
    	
    }
    
    private Node find(Node n,Key key) {
	if(n.membernum == 0)
		return null;
//	if(n.membernum == t-1 && !n.equals(root))
//		merge(n);
    	if(less(key,n.members[0].key))
    	{
		
    		if(n.childnum > 0 )
    			return find(n.children[0],key);
    		else
    			return null;
    	}
    	else if(greater(key,n.members[n.membernum-1].key))
    	{
    		if(n.childnum >0)
    			return find(n.children[n.childnum-1],key);
    		else
    			return null;
    	}
    	for(int i=0;i<n.membernum;i++)
    	{
    		if(equal(key,n.members[i].key))
    		{
    			//n.members[i].key = null;
			if(!n.equals(root))
			{
			//	if(n.parent.parent !=null)
			//	father = n.parent.parent;
				
			}
   			return n;
    		}
    		if(i!=n.membernum-1)
    		{
    			if(greater(key,n.members[i].key)&&less(key,n.members[i+1].key))
    			{
    				if(n.childnum>0)
    					return find(n.children[i+1],key);
    				else
    					return null;
    			}
    		}
    	}
    	return null;
    	
    }
    
    /*
    private List<Value> find(Node n ,Key key,int ht,List<Value> val)
    {
    	//== if we are at leaves==
    	if(ht==0)
    	{
    		for(int i=0;i<n.childnum;i++)
    		{
    			if(equal(key,n.children[i].key))
    				val.add((Value)n.children[i].obj);
    			if(greater(key,n.children[i].key))
    				return val;
    		}
    	}
    	else
    	{
    		for(int j=0;j<n.childnum;j++)
    		{
    			if(less(key,n.children[j+1].key))
    				return find(n,key,ht-1,val);  //===========need to make some correction here==============
    		}
    	}
    	
    	return val;
    }*/
    
    private boolean greater(Comparable key1,Comparable key2)
    {
    	if(key1.compareTo(key2) > 0)
    		return true;
    	return false;
    }
    private boolean less (Comparable key1,Comparable key2) {
    	if(key1.compareTo(key2) < 0)
    		return true;
    	return false;
    }
    private boolean equal(Comparable key1,Comparable key2)
    {
    	return key1.compareTo(key2)==0;
    }
}

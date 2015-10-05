package pathfinding;
import java.util.Vector;

public class MapGrid {

	public float minX, maxX, minY, maxY;

	public int idCase = 0;

	public Vector<Vector<Case>> grid;
	public Vector<Float> Xcoord;
	public Vector<Float> Ycoord;

	public MapGrid(float minX, float maxX, float minY, float maxY){
		grid = new Vector<Vector<Case>>();
		grid.add(new Vector<Case>());
		grid.get(0).add(new Case(true,idCase,this));
		idCase++;
		grid.get(0).get(0).update(minX, minY, maxX, maxY);
		Xcoord = new Vector<Float>();
		Xcoord.add(minX);
		Xcoord.add(maxX);
		Ycoord = new Vector<Float>();
		Ycoord.add(minY);
		Ycoord.add(maxY);
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}

	public void insertNewX(float f){
		//		System.out.println(this);
		if(Xcoord.contains(f) || f<0f || f>Xcoord.get(Xcoord.size()-1))
			return;
		int i=0;
		while(i<Xcoord.size() && Xcoord.get(i)<f)
			i++;
		float f1 = Xcoord.get(i-1);
		float f2 = Xcoord.get(i);
		Xcoord.insertElementAt(f, i);
		grid.insertElementAt(new Vector<Case>(), i-1);
		for(int j=0; j<Ycoord.size()-1; j++){
			grid.get(i-1).add(new Case(grid.get(i).get(j).ok,idCase,this));
			idCase++;
			grid.get(i-1).get(j).updateX(f1, f);
			grid.get(i-1).get(j).updateY(Ycoord.get(j),Ycoord.get(j+1));
			grid.get(i).get(j).updateX(f, f2);
		}
		//		for(int k =0; k<this.grid.size(); k++)
		//			System.out.println(grid.get(k).get(0));
	}

	public void insertNewY(float f){
		//		System.out.println(this);
		if(Ycoord.contains(f) || f<0f || f>Ycoord.get(Ycoord.size()-1))
			return;
		int j=0;
		while(j<Ycoord.size() && Ycoord.get(j)<f)
			j++;
		float f1 = Ycoord.get(j-1);
		float f2 = Ycoord.get(j);
		Ycoord.insertElementAt(f, j);
		for(int i=0; i<Xcoord.size()-1; i++){
			grid.get(i).insertElementAt(new Case(grid.get(i).get(j-1).ok,idCase,this), j-1);
			idCase++;
			grid.get(i).get(j-1).updateX(Xcoord.get(i),Xcoord.get(i+1));
			grid.get(i).get(j-1).updateY(f1,f);
			grid.get(i).get(j).updateY(f, f2);
		}
		//		for(int k =0; k<this.grid.get(0).size(); k++)
		//			System.out.println(grid.get(0).get(k));
	}

	public String toString(){
		String s ="";
		s+="Xcoord : ";
		for(Float f: Xcoord)
			s+= f+" ";
		s+="\nYcoord : ";
		for(Float f: Ycoord)
			s+= f+" ";
		s+="\n";
		for(int j=0; j<grid.get(0).size(); j++){
			for(int i=0; i<grid.size();i++){
				s+=(grid.get(i).get(j).ok ? "O " : "X ");
			}
			s+="\n";
		}
		return s;
	}

	public void insertNewRec(float X, float Y, float sizeX, float sizeY){
		insertNewX(X-sizeX/2f);
		insertNewX(X+sizeX/2f);
		insertNewY(Y-sizeY/2f);
		insertNewY(Y+sizeY/2f);
		//		int imin = (X-sizeX/2f<0 ? 0: Xcoord.indexOf(X-sizeX/2f));
		//		int imax = (X+sizeX/2f>maxX ? grid.size()-1 : Xcoord.indexOf(X+sizeX/2f));
		//		int jmin = (Y-sizeY/2f<0 ? 0: Ycoord.indexOf(Y-sizeY/2f));
		//		int jmax = (Y+sizeY/2f>maxY ? grid.get(0).size()-1 : Ycoord.indexOf(Y+sizeY/2f));
		int imin = (Xcoord.indexOf(X-sizeX/2f));
		int imax = (Xcoord.indexOf(X+sizeX/2f));
		int jmin = (Ycoord.indexOf(Y-sizeY/2f));
		int jmax = (Ycoord.indexOf(Y+sizeY/2f));
		for(int i = imin;i<imax;i++ )
			for(int j = jmin; j<jmax; j++)
				if(i>=0 && i<grid.size() && j>=0 && j<grid.get(0).size())
					grid.get(i).get(j).ok = false;
	}

	public Case getCase(float x, float y){
		if(x<minX || x>=maxX || y<minY||y>=maxY)
			return null;
		int i=0, j=0;
		while(x>Xcoord.get(i+1))
			i++;
		while(y>Ycoord.get(j+1))
			j++;
		return grid.get(i).get(j);
	}

	public Vector<Case> pathfinding(float xStart, float yStart, float xEnd, float yEnd){
		System.out.println("MapGrid line 124: calcul d'un chemin");
		Vector<Case> path = new Vector<Case>();
		int iStart=0, jStart=0, iEnd=0, jEnd=0;
		while(xStart>Xcoord.get(iStart+1))
			iStart++;
		while(yStart>Ycoord.get(jStart+1))
			jStart++;
		while(xEnd>Xcoord.get(iEnd+1))
			iEnd++;
		while(yEnd>Ycoord.get(jEnd+1))
			jEnd++;
		Vector<Point> closedList = new Vector<Point>();
		Vector<Point> openList = new Vector<Point>();
		Point depart = new Point(iStart,jStart,this);
		depart.computeDistance(iEnd, jEnd);
		openList.add(depart);
		Point u;
		Point v;
		int indice;
		int iTrav=0, jTrav=0;
		int nbIt = 0;
		while(openList.size()>0){
			nbIt++;
			u = openList.firstElement();
			openList.remove(0);
			// voisin de droite
			for(int k=0;k<8;k++){
				switch(k){
				case 0: //� droite
					iTrav = u.i+1; jTrav = u.j;break;
				case 1: //� gauche
					iTrav = u.i-1; jTrav = u.j;break;
				case 2: //en bas
					iTrav = u.i; jTrav = u.j+1;break;
				case 3: //en haut
					iTrav = u.i; jTrav = u.j-1;break;
				case 4: //en bas � droite
					iTrav = u.i+1; jTrav = u.j+1;break;
				case 5: //en bas � gauche
					iTrav = u.i-1; jTrav = u.j+1;break;
				case 6: //en haut � droite
					iTrav = u.i+1; jTrav = u.j-1;break;
				case 7: //en haut � gauche
					iTrav = u.i-1; jTrav = u.j-1;break;
				}
				if(iTrav==iEnd && jTrav==jEnd){
					path.add(grid.get(iTrav).get(jTrav));
					while(u!=null){
						path.insertElementAt(grid.get(u.i).get(u.j),0);
						u = u.comeFrom;
					}
					return path;
				}
				if(iTrav>=0&& iTrav<grid.size() && jTrav>=0&& jTrav<grid.get(0).size() && grid.get(iTrav).get(jTrav).ok){
					if(k<4 || (grid.get(iTrav).get(u.j).ok && grid.get(u.i).get(jTrav).ok)){

						v = new Point(iTrav,jTrav,this,u,(k<4?1f:1.5f));
						boolean doNothing = false;
						Point toDelete = null;
						for(Point p : closedList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}
						if(toDelete!=null)
							closedList.remove(toDelete);
						if(doNothing)
							continue;
						toDelete = null;
						for(Point p : openList){
							if(p.id==v.id){
								if(p.cost<v.cost)
									doNothing =true;
								else
									toDelete = p;
							}
						}

						if(toDelete!=null)
							openList.remove(toDelete);
						if(doNothing)
							continue;
						v.computeDistance(iEnd, jEnd);
						v.heuristique = v.cost + v.dist;
						indice= 0;
						while(indice<openList.size() && openList.get(indice).heuristique<v.heuristique)
							indice++;
						openList.insertElementAt(v,indice);
					}
				}
			}
			closedList.addElement(u);

		}

		return path;
	}

	public void printPath(Vector<Case> path){
		Case c;
		for(int j=0; j<grid.get(0).size();j++){
			for(int i=0; i<grid.size(); i++){
				c = grid.get(i).get(j);
				if(c.ok){
					if(path.contains(c))
						System.out.print("& ");
					else
						System.out.print("O ");
				} else {
					if(path.contains(c))
						System.out.print("? ");
					else
						System.out.print("X ");

				}
			}
			System.out.println("");
		}
	}

	public class Point {
		public int i;
		public int j;
		public int id;
		public float cost, heuristique;
		public float dist;
		public Point comeFrom;
		public MapGrid map;
		public Point(int i, int j, MapGrid map){
			this.i = i;
			this.j = j;
			this.map = map;
			this.id = map.grid.get(i).get(j).id;
		}
		public Point(int i, int j, MapGrid map, Point comeFrom, float addCost){
			this.i = i;
			this.j = j;
			this.map = map;
			this.id = map.grid.get(i).get(j).id;
			this.cost=comeFrom.cost+addCost;
			this.comeFrom = comeFrom;
		}
		public void computeDistance(int iEnd, int jEnd){
			this.dist = (float)Math.sqrt((i-iEnd)*(i-iEnd)+(j-jEnd)*(j-jEnd));
		}

	}

}


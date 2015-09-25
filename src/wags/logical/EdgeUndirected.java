package wags.logical;


import org.vaadin.gwtgraphics.client.Line;

import wags.logical.view.LogicalProblem;
import wags.logical.TreeProblems.TreeDisplayManager;
import wags.logical.view.LogicalPanelUi;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;


public class EdgeUndirected extends EdgeParent implements IsSerializable
{
	
	//I'm sorry about the magic, buuut the nodes are 40x40, this evens out the getLeft() and getTop() method results
	public static final int OFF = 20;
	public boolean removable;
	
	public void addWeightLabel(String incomingWeight){
		// MAGIC, MAGIC EVERYWHERE... MAY GOD HAVE MERCY ON MY SOUL       
		// Why you ask? Because Chrome.
		weight = Integer.parseInt(incomingWeight);
		Window.alert("AWL EU n1:" +n1+" n2:" +n2 + " line:"+line);
		int width = Math.abs(n1.getLeft()-n2.getLeft());     // getOffset width returns a big fat 0 so we get an offset from the two nodes on the edge.
		int height = Math.abs(n1.getTop()-n2.getTop());      // getOffset height returns a big fat 0 as well so we again get an offset from a two friendly neighborhood nodes.
		int midVert = (((line.getAbsoluteTop()-154)+((line.getAbsoluteTop()-154)+(height-15)))/2+125);              // stuff
		int midHoriz = ((line.getAbsoluteLeft()+(line.getAbsoluteLeft()+width))/2);                                 // moar stuff
		Window.alert("Weight is: " + weight);
		Label l = new Label(weight+"");
		l.setStyleName("edge_weight");
		Label lab = new Label();
		ec.addWeightLabel(l, midHoriz, midVert, this);
	}
	public EdgeUndirected(){super(null,null,null,null,false);}
	
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, ClickHandler ch, boolean removable)
	{
		super(n1, n2, ec, ch, removable);
	}
	
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, boolean removable) {
		super(n1, n2, ec, null, removable);	
		this.removable = removable;
	}
	
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec)
	{
		super(n1, n2, ec, null, false);
	}
	public EdgeUndirected(Node n1, Node n2, EdgeCollection ec, ClickHandler ch, boolean removable, int weight)
	{
		super(n1, n2, ec, ch, removable, weight);
	}
	
	public EdgeUndirected(EdgeCollection ec, ClickHandler ch, boolean removable) {
		super(null,null,ec,ch,removable);
	}

	public EdgeUndirected(EdgeCollection ec, boolean removable) {
		super(null,null,ec,null,removable);
		this.removable = removable;
		}
	
	@Override
	public void drawEdge() {

		if(n1 != null && n2 != null) {
		line = new Line(n1.getLeft(), 
						n1.getTop(),
						n2.getLeft(), 
						n2.getTop());
		if (removable) {
			line.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					Line selected = (Line)event.getSource();
					selected.setStrokeColor("yellow");
					//REMOVEEDGES
					if(Window.confirm("Would you like to remove this edge?")) { 
						ec.removeEdgeFromCanvas(selected);
					}
					else
						selected.setStrokeColor("#444");
						
				}
			});
		}
		line.setStrokeColor("#444");
		line.setStrokeWidth(3);
		super.setLine(line);
		ec.addEdge(this);
		ec.addLine(line);
		ec.addEdgeToCanvas(line);
		}	
	}
	
	@Override
	public void drawEdges(int[][] lineDims) {		
		
		//Create edges using 2D array of line dimensions passed in by EdgeCollection
		for (int i = 0; i < lineDims.length; i++) {
			line = new Line(lineDims[i][0] + OFF,
							lineDims[i][1] + OFF,
							lineDims[i][2] + OFF,
							lineDims[i][3] + OFF);
			switch (LogicalPanelUi.getGenre()) {
			case "traversal":
			case "heapInsert":
				if (removable) {
					line.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							Line selected = (Line)event.getSource();
							selected.setStrokeColor("yellow");
							if(Window.confirm("Would you like to remove this edge?")) 
								LogicalPanelUi.getPanel().remove(selected);
							else
								selected.setStrokeColor("#444");
						}
					});
				}
				break;
			case "mst":
				
				line.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						Line selected = (Line)event.getSource();
						
						selected.setStrokeColor("#27f500");
						
					    getN1().getLabel().setStyleName("immobilized_node");
					    getN2().getLabel().setStyleName("immobilized_node");							
					}
				});
				break;
			}
			line.setStrokeColor("#444");
			line.setStrokeWidth(3);
			super.setLine(line);
			//addWeightLabel();
			ec.addEdgeToCanvas(line);
		}
	}
}

// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:35


package rs.ac.bg.etf.pp1.ast;

public class FactorDesignatorHasActs extends Factor {

    private Designator Designator;
    private DesigOptActLeftParen DesigOptActLeftParen;
    private OptActualParams OptActualParams;

    public FactorDesignatorHasActs (Designator Designator, DesigOptActLeftParen DesigOptActLeftParen, OptActualParams OptActualParams) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.DesigOptActLeftParen=DesigOptActLeftParen;
        if(DesigOptActLeftParen!=null) DesigOptActLeftParen.setParent(this);
        this.OptActualParams=OptActualParams;
        if(OptActualParams!=null) OptActualParams.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public DesigOptActLeftParen getDesigOptActLeftParen() {
        return DesigOptActLeftParen;
    }

    public void setDesigOptActLeftParen(DesigOptActLeftParen DesigOptActLeftParen) {
        this.DesigOptActLeftParen=DesigOptActLeftParen;
    }

    public OptActualParams getOptActualParams() {
        return OptActualParams;
    }

    public void setOptActualParams(OptActualParams OptActualParams) {
        this.OptActualParams=OptActualParams;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(DesigOptActLeftParen!=null) DesigOptActLeftParen.accept(visitor);
        if(OptActualParams!=null) OptActualParams.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(DesigOptActLeftParen!=null) DesigOptActLeftParen.traverseTopDown(visitor);
        if(OptActualParams!=null) OptActualParams.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(DesigOptActLeftParen!=null) DesigOptActLeftParen.traverseBottomUp(visitor);
        if(OptActualParams!=null) OptActualParams.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorDesignatorHasActs(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesigOptActLeftParen!=null)
            buffer.append(DesigOptActLeftParen.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptActualParams!=null)
            buffer.append(OptActualParams.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorDesignatorHasActs]");
        return buffer.toString();
    }
}

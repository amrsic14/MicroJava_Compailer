// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:35


package rs.ac.bg.etf.pp1.ast;

public class ArrayDesignator extends Designator {

    private ArrDesig ArrDesig;
    private Expr Expr;

    public ArrayDesignator (ArrDesig ArrDesig, Expr Expr) {
        this.ArrDesig=ArrDesig;
        if(ArrDesig!=null) ArrDesig.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ArrDesig getArrDesig() {
        return ArrDesig;
    }

    public void setArrDesig(ArrDesig ArrDesig) {
        this.ArrDesig=ArrDesig;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrDesig!=null) ArrDesig.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrDesig!=null) ArrDesig.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrDesig!=null) ArrDesig.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayDesignator(\n");

        if(ArrDesig!=null)
            buffer.append(ArrDesig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayDesignator]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 23/5/2020 15:56:20


package rs.ac.bg.etf.pp1.ast;

public class RightMulop extends Mulop {

    private MulopRight MulopRight;

    public RightMulop (MulopRight MulopRight) {
        this.MulopRight=MulopRight;
        if(MulopRight!=null) MulopRight.setParent(this);
    }

    public MulopRight getMulopRight() {
        return MulopRight;
    }

    public void setMulopRight(MulopRight MulopRight) {
        this.MulopRight=MulopRight;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MulopRight!=null) MulopRight.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MulopRight!=null) MulopRight.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MulopRight!=null) MulopRight.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("RightMulop(\n");

        if(MulopRight!=null)
            buffer.append(MulopRight.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [RightMulop]");
        return buffer.toString();
    }
}

// generated with ast extension for cup
// version 0.8
// 16/7/2020 23:21:35


package rs.ac.bg.etf.pp1.ast;

public class IfStatement extends Statement {

    private IfCondBegin IfCondBegin;
    private IfCond IfCond;
    private Statement Statement;

    public IfStatement (IfCondBegin IfCondBegin, IfCond IfCond, Statement Statement) {
        this.IfCondBegin=IfCondBegin;
        if(IfCondBegin!=null) IfCondBegin.setParent(this);
        this.IfCond=IfCond;
        if(IfCond!=null) IfCond.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public IfCondBegin getIfCondBegin() {
        return IfCondBegin;
    }

    public void setIfCondBegin(IfCondBegin IfCondBegin) {
        this.IfCondBegin=IfCondBegin;
    }

    public IfCond getIfCond() {
        return IfCond;
    }

    public void setIfCond(IfCond IfCond) {
        this.IfCond=IfCond;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfCondBegin!=null) IfCondBegin.accept(visitor);
        if(IfCond!=null) IfCond.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfCondBegin!=null) IfCondBegin.traverseTopDown(visitor);
        if(IfCond!=null) IfCond.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfCondBegin!=null) IfCondBegin.traverseBottomUp(visitor);
        if(IfCond!=null) IfCond.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfStatement(\n");

        if(IfCondBegin!=null)
            buffer.append(IfCondBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfCond!=null)
            buffer.append(IfCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfStatement]");
        return buffer.toString();
    }
}

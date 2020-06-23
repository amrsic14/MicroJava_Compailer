// generated with ast extension for cup
// version 0.8
// 23/5/2020 15:34:22


package rs.ac.bg.etf.pp1.ast;

public class ForStatement extends Statement {

    private OptDesigStatement OptDesigStatement;
    private ForFirstSemi ForFirstSemi;
    private OptCond OptCond;
    private OptDesigStatement OptDesigStatement1;
    private ForParenEnd ForParenEnd;
    private Statement Statement;

    public ForStatement (OptDesigStatement OptDesigStatement, ForFirstSemi ForFirstSemi, OptCond OptCond, OptDesigStatement OptDesigStatement1, ForParenEnd ForParenEnd, Statement Statement) {
        this.OptDesigStatement=OptDesigStatement;
        if(OptDesigStatement!=null) OptDesigStatement.setParent(this);
        this.ForFirstSemi=ForFirstSemi;
        if(ForFirstSemi!=null) ForFirstSemi.setParent(this);
        this.OptCond=OptCond;
        if(OptCond!=null) OptCond.setParent(this);
        this.OptDesigStatement1=OptDesigStatement1;
        if(OptDesigStatement1!=null) OptDesigStatement1.setParent(this);
        this.ForParenEnd=ForParenEnd;
        if(ForParenEnd!=null) ForParenEnd.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
    }

    public OptDesigStatement getOptDesigStatement() {
        return OptDesigStatement;
    }

    public void setOptDesigStatement(OptDesigStatement OptDesigStatement) {
        this.OptDesigStatement=OptDesigStatement;
    }

    public ForFirstSemi getForFirstSemi() {
        return ForFirstSemi;
    }

    public void setForFirstSemi(ForFirstSemi ForFirstSemi) {
        this.ForFirstSemi=ForFirstSemi;
    }

    public OptCond getOptCond() {
        return OptCond;
    }

    public void setOptCond(OptCond OptCond) {
        this.OptCond=OptCond;
    }

    public OptDesigStatement getOptDesigStatement1() {
        return OptDesigStatement1;
    }

    public void setOptDesigStatement1(OptDesigStatement OptDesigStatement1) {
        this.OptDesigStatement1=OptDesigStatement1;
    }

    public ForParenEnd getForParenEnd() {
        return ForParenEnd;
    }

    public void setForParenEnd(ForParenEnd ForParenEnd) {
        this.ForParenEnd=ForParenEnd;
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
        if(OptDesigStatement!=null) OptDesigStatement.accept(visitor);
        if(ForFirstSemi!=null) ForFirstSemi.accept(visitor);
        if(OptCond!=null) OptCond.accept(visitor);
        if(OptDesigStatement1!=null) OptDesigStatement1.accept(visitor);
        if(ForParenEnd!=null) ForParenEnd.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptDesigStatement!=null) OptDesigStatement.traverseTopDown(visitor);
        if(ForFirstSemi!=null) ForFirstSemi.traverseTopDown(visitor);
        if(OptCond!=null) OptCond.traverseTopDown(visitor);
        if(OptDesigStatement1!=null) OptDesigStatement1.traverseTopDown(visitor);
        if(ForParenEnd!=null) ForParenEnd.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptDesigStatement!=null) OptDesigStatement.traverseBottomUp(visitor);
        if(ForFirstSemi!=null) ForFirstSemi.traverseBottomUp(visitor);
        if(OptCond!=null) OptCond.traverseBottomUp(visitor);
        if(OptDesigStatement1!=null) OptDesigStatement1.traverseBottomUp(visitor);
        if(ForParenEnd!=null) ForParenEnd.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ForStatement(\n");

        if(OptDesigStatement!=null)
            buffer.append(OptDesigStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForFirstSemi!=null)
            buffer.append(ForFirstSemi.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptCond!=null)
            buffer.append(OptCond.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptDesigStatement1!=null)
            buffer.append(OptDesigStatement1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForParenEnd!=null)
            buffer.append(ForParenEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ForStatement]");
        return buffer.toString();
    }
}

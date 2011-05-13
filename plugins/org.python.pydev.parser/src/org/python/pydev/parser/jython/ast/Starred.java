// Autogenerated AST node
package org.python.pydev.parser.jython.ast;
import org.python.pydev.parser.jython.SimpleNode;

public final class Starred extends exprType implements expr_contextType {
    public exprType value;
    public int ctx;

    public Starred(exprType value, int ctx) {
        this.value = value;
        this.ctx = ctx;
    }


    public Starred createCopy() {
        return createCopy(true);
    }
    public Starred createCopy(boolean copyComments) {
        Starred temp = new Starred(value!=null?(exprType)value.createCopy(copyComments):null, ctx);
        temp.beginLine = this.beginLine;
        temp.beginColumn = this.beginColumn;
        if(this.specialsBefore != null && copyComments){
            for(Object o:this.specialsBefore){
                if(o instanceof commentType){
                    commentType commentType = (commentType) o;
                    temp.getSpecialsBefore().add(commentType.createCopy(copyComments));
                }
            }
        }
        if(this.specialsAfter != null && copyComments){
            for(Object o:this.specialsAfter){
                if(o instanceof commentType){
                    commentType commentType = (commentType) o;
                    temp.getSpecialsAfter().add(commentType.createCopy(copyComments));
                }
            }
        }
        return temp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Starred[");
        sb.append("value=");
        sb.append(dumpThis(this.value));
        sb.append(", ");
        sb.append("ctx=");
        sb.append(dumpThis(this.ctx, expr_contextType.expr_contextTypeNames));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitStarred(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (value != null){
            value.accept(visitor);
        }
    }

}

// Autogenerated AST node
package org.python.pydev.parser.jython.ast;
import org.python.pydev.parser.jython.SimpleNode;

public final class Dict extends exprType {
    public exprType[] keys;
    public exprType[] values;

    public Dict(exprType[] keys, exprType[] values) {
        this.keys = keys;
        this.values = values;
    }


    public Dict createCopy() {
        return createCopy(true);
    }
    public Dict createCopy(boolean copyComments) {
        exprType[] new0;
        if(this.keys != null){
        new0 = new exprType[this.keys.length];
        for(int i=0;i<this.keys.length;i++){
            new0[i] = (exprType) (this.keys[i] != null? this.keys[i].createCopy(copyComments):null);
        }
        }else{
            new0 = this.keys;
        }
        exprType[] new1;
        if(this.values != null){
        new1 = new exprType[this.values.length];
        for(int i=0;i<this.values.length;i++){
            new1[i] = (exprType) (this.values[i] != null?
            this.values[i].createCopy(copyComments):null);
        }
        }else{
            new1 = this.values;
        }
        Dict temp = new Dict(new0, new1);
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
        StringBuffer sb = new StringBuffer("Dict[");
        sb.append("keys=");
        sb.append(dumpThis(this.keys));
        sb.append(", ");
        sb.append("values=");
        sb.append(dumpThis(this.values));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitDict(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] != null){
                    keys[i].accept(visitor);
                }
            }
        }
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null){
                    values[i].accept(visitor);
                }
            }
        }
    }

}

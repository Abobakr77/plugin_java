package com.upm.fixer;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.printer.YamlPrinter;
import com.upm.util.MessageDisplay;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static com.upm.util.ASTHelper.changeVariableType;
import static com.upm.util.ASTHelper.getVariableType;

public interface CodeSmellMinorFixer {



    static boolean RSPEC_1596_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-1596";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);

        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getFields().forEach(f -> {
                f.asFieldDeclaration().getVariables().forEach(v -> {

                    if(v.getInitializer().get().isFieldAccessExpr()) {
                        if("EMPTY_LIST".equals(v.getInitializer().get().asFieldAccessExpr().getName().getIdentifier()))
                        {
                            MessageDisplay.startFixingMessage(documentationURI);
                            v.setInitializer("Collections.emptyList()");
                            MessageDisplay.finishFixingMessage(documentationURI);


                            isFound.set(true);
                            return;
                        }
                        if("EMPTY_MAP".equals(v.getInitializer().get().asFieldAccessExpr().getName().getIdentifier()))
                        {
                            MessageDisplay.startFixingMessage(documentationURI);
                            v.setInitializer("Collections.emptyMap()");
                            MessageDisplay.finishFixingMessage(documentationURI);
                            isFound.set(true);
                            return;
                        }
                        if("EMPTY_SET".equals(v.getInitializer().get().asFieldAccessExpr().getName().getIdentifier()))
                        {
                            MessageDisplay.startFixingMessage(documentationURI);
                            v.setInitializer("Collections.emptySet()");
                            MessageDisplay.finishFixingMessage(documentationURI);
                            isFound.set(true);
                            return;
                        }



                    }









                });


            });
        });
        return isFound.get();
    }
    static boolean RSPEC_1155_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-1155";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);

        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(e -> {
                e.getBody().get().getStatements().forEach(st -> {
                    if(st.isIfStmt()){
                        if(st.asIfStmt().getCondition().isBinaryExpr()){
                            if(st.asIfStmt().getCondition().asBinaryExpr().getLeft().isMethodCallExpr()){
                                if("size".equals(st.asIfStmt().getCondition().asBinaryExpr().getLeft().asMethodCallExpr().getName().getIdentifier())){


                                    MessageDisplay.startFixingMessage(documentationURI);
                                    String ref= st.asIfStmt().getCondition().asBinaryExpr().getLeft().asMethodCallExpr().getScope().get().asNameExpr().getName().getIdentifier();
                                    st.asIfStmt().setCondition(new NameExpr(""+ref+".isEmpty()"));
                                    MessageDisplay.finishFixingMessage(documentationURI);
                                    isFound.set(true);
                                }
                            }
                        }
                    }
                });
            });
        });
        return isFound.get();
    }
    static boolean RSPEC_1132_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-1132";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);

        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {

                method.getBody().get().getStatements().forEach(st -> {
                    if(st.isIfStmt()){
                        if(st.asIfStmt().getCondition().isMethodCallExpr()){
                            if(st.asIfStmt().getCondition().asMethodCallExpr().getScope().get().isNameExpr() &&
                                    st.asIfStmt().getCondition().asMethodCallExpr().getArgument(0).isStringLiteralExpr()
                                    &&  st.asIfStmt().getCondition().asMethodCallExpr().getName().asString().equals("equals")
                            ){


                                MessageDisplay.startFixingMessage(documentationURI);

                                String scope = st.asIfStmt().getCondition().asMethodCallExpr().getScope().get().asNameExpr().getName().getIdentifier();

                                String stringLiteralExpr = st.asIfStmt().getCondition().asMethodCallExpr().getArgument(0).asStringLiteralExpr().getValue();
                                st.asIfStmt().setCondition(new NameExpr("\""+stringLiteralExpr+"\".equals("+scope+")"));

                                MessageDisplay.finishFixingMessage(documentationURI);

                                isFound.set(true);
                            }



                        }
                    }
                });
            });
        });


        return isFound.get();
    }
    static boolean RSPEC_4973_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-4973";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);

        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {

                method.getBody().get().getStatements().forEach(st -> {
                    if(st.isIfStmt()){
                        if(st.asIfStmt().getCondition().isBinaryExpr()){
                            if(st.asIfStmt().getCondition().asBinaryExpr().getOperator().asString().equals("==")
                                    && st.asIfStmt().getCondition().asBinaryExpr().getLeft().isNameExpr()
                                    && st.asIfStmt().getCondition().asBinaryExpr().getRight().isNameExpr()
                            ){

                                MessageDisplay.startFixingMessage(documentationURI);

                                String leftvar =  st.asIfStmt().getCondition().asBinaryExpr().getLeft().asNameExpr().getNameAsString();
                                String rightvar =  st.asIfStmt().getCondition().asBinaryExpr().getRight().asNameExpr().getNameAsString();

                                st.asIfStmt().setCondition(new NameExpr(""+leftvar+" != null && "+leftvar+".equals("+rightvar+")"));


                                MessageDisplay.finishFixingMessage(documentationURI);

                                isFound.set(true);

                            }




                        }
                    }
                });
            });
        });


        return isFound.get();
    }
    static boolean RSPEC_1643_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-1643";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);
        AtomicReference<String> varname= new AtomicReference<>("");
        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {

                method.getBody().get().asBlockStmt().getStatements().stream().forEach(statement -> {
                    if(statement.isForStmt()){

                        statement.asForStmt().getBody()
                                .asBlockStmt().getStatements().stream().forEach(blockStatement->{

                            if(blockStatement.isExpressionStmt() ){


                                if(blockStatement.asExpressionStmt()
                                        .getExpression().isAssignExpr()
                                        &&  blockStatement.asExpressionStmt()
                                        .getExpression()
                                        .asAssignExpr()
                                        .getValue()
                                        .asBinaryExpr()
                                        .getOperator().asString().equals("+")
                                        &&
                                        getVariableType(cu, blockStatement.asExpressionStmt()
                                                .getExpression()
                                                .asAssignExpr()
                                                .getValue()
                                                .asBinaryExpr()
                                                .getLeft()
                                                .asNameExpr()
                                                .getName()
                                                .getIdentifier()).equals(
                                                getVariableType(cu, blockStatement.asExpressionStmt()
                                                        .getExpression()
                                                        .asAssignExpr()
                                                        .getTarget()
                                                        .asNameExpr()
                                                        .getName()
                                                        .getIdentifier()
                                                ))){


                                    MessageDisplay.startFixingMessage(documentationURI);



                                    varname.set(blockStatement.asExpressionStmt()
                                            .getExpression()
                                            .asAssignExpr()
                                            .getValue()
                                            .asBinaryExpr()
                                            .getLeft()
                                            .asNameExpr()
                                            .getName()
                                            .getIdentifier());


                                    NameExpr nameExpr = new NameExpr(varname.get());
                                    MethodCallExpr methodCallExpr = new MethodCallExpr(nameExpr, "append");
                                    methodCallExpr.addArgument( blockStatement.asExpressionStmt()
                                            .getExpression()
                                            .asAssignExpr()
                                            .getValue()
                                            .asBinaryExpr()
                                            .getRight()
                                            .asArrayAccessExpr()
                                    );
                                    blockStatement
                                            .asExpressionStmt()
                                            .setExpression(methodCallExpr);




                                }

                            }
                        });

                    }
                });
                if(!varname.get().isEmpty()) {
                    changeVariableType(cu, varname.get());

                    MessageDisplay.finishFixingMessage(documentationURI);

                    isFound.set(true);

                }





            });
        });


        return isFound.get();
    }
    static boolean RSPEC_2130_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-2130";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);

        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {

                List<Statement> myList = new CopyOnWriteArrayList<>();
                myList.addAll(method.getBody().get().asBlockStmt().getStatements());

                Iterator<Statement> statementIterator =   myList.iterator();


                while (statementIterator.hasNext()) {
                    Statement statement = statementIterator.next();
                    if(statement.isExpressionStmt()){
                        if(statement.asExpressionStmt().getExpression().isVariableDeclarationExpr()){

                            Iterator<VariableDeclarator> iter =     statement.asExpressionStmt().getExpression()
                                    .asVariableDeclarationExpr().getVariables().iterator();

                            while (iter.hasNext()) {
                                VariableDeclarator variableDeclarator = iter.next();
                                if(variableDeclarator.getType().asString().equals("float")) {

                                    MessageDisplay.startFixingMessage(documentationURI);

                                    variableDeclarator.setInitializer("Float.parseFloat("+variableDeclarator.getInitializer().get().asMethodCallExpr().getScope().get().asEnclosedExpr().getInner().asObjectCreationExpr().getArgument(0)+")");

                                    MessageDisplay.finishFixingMessage(documentationURI);

                                    isFound.set(true);


                                }

                            }

                        }

                    }
                }

            });
        });


        return isFound.get();
    }
    static boolean RSPEC_1186_FIX(CompilationUnit cu) {
        final String documentationURI = "java/type/Bug/RSPEC-1186";
        AtomicReference<Boolean> isFound = new AtomicReference<>(false);


        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {

                if(method.getBody().get().getStatements().isEmpty()){
                    MessageDisplay.startFixingMessage(documentationURI);
                    method.getBody().get().addOrphanComment(new JavadocComment("Method should not be empty"));
                    MessageDisplay.finishFixingMessage(documentationURI);
                    isFound.set(true);
                }

            });
        });


        return isFound.get();
    }

}

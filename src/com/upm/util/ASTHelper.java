package com.upm.util;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public interface ASTHelper {

    static String  getVariableType(CompilationUnit cu, String variableName){
        AtomicReference<String> returnedValue= new AtomicReference<>("");
        cu.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
            c.getMethods().forEach(method -> {
                if(method.getBody().get().isExpressionStmt()){
                    if(method.getBody().get().asExpressionStmt().getExpression().isVariableDeclarationExpr()){
                        method.getBody().get().asExpressionStmt().getExpression()
                                .asVariableDeclarationExpr().getVariables().forEach(v->{

                            if(variableName.equals(v.getName())){
                                returnedValue.set(v.getType().asClassOrInterfaceType().getName().getIdentifier());

                            }
                        });
                    }


                }
            });
        });





        return returnedValue.get();
    }


    static void  changeVariableType(CompilationUnit ce, String variableName){
        ce.findAll(ClassOrInterfaceDeclaration.class).stream().forEach(c -> {
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

                                if(variableName.equals(variableDeclarator.getName().asString())) {
                                    System.out.println("line = " + variableDeclarator.getBegin().get().line);
                                    statement.remove();
                                    method.getBody().get().addStatement(0,
                                            createAndGetVariable(
                                                    variableName,
                                                    "StringBuilder",
                                                    "new StringBuilder()"));
                                    // statement.asExpressionStmt().setExpression( createAndGetVariable("variableName", "me", "new me()"));
                                    //  method.getBody().get().asBlockStmt().getStatements().remove(tobedeleted);


                                    break;

                                    // variableDeclarator.setName("chs")
                                    //method.remove(variableDeclarator);
                                    // statement.remove(variableDeclarator);
                                    //variableDeclarator.setType(new ClassOrInterfaceType("StringBuilder"));
                                    //  variableDeclarator.setInitializer("new StringBuilder()");
                                }

                            }
                       /*   statement.asExpressionStmt().getExpression()
                                    .asVariableDeclarationExpr().getVariables().forEach(v->{



                                if(variableName.equals(v.getName().asString())){
                                    System.out.println( "line = "+v.getBegin().get().line);
                                    method.getBody().get().addStatement(v.getBegin().get().line,createAndGetVariable("variableName","me","new me()"));

                                  //  statement.as("StringBuilder bld = new StringBuilder();");

                                  *//*  statement.asExpressionStmt().getExpression()
                                            .asVariableDeclarationExpr().setVariables(new NodeList(createAndGetVariable(
                                            "bld",
                                            "StringBuilder",
                                            "new StringBuilder()")));*//*
                            //       v.setInitializer("new StringBuilder()");
                              //      v.setType(new ClassOrInterfaceType("StringBuilder"));

*//*
                                    method.accept(new ModifierVisitor<Visitable>(){
                                        @Override
                                        public Visitable visit(VariableDeclarator n, Visitable arg) {
                                            // return super.visit(n, arg);

                                            if(variableName.equals(n.getName().asString())){
                                              // n.remove();
                                                VariableDeclarator variableDeclarator = new VariableDeclarator();
                                                variableDeclarator.setName("variableName");
                                                variableDeclarator.setType(new ClassOrInterfaceType("variableType"));
                                                variableDeclarator.setInitializer("varaibleInitializer");
                                                 //n.removeInitializer();
                                                 //= variableDeclarator;

                                              //  System.out.println( "line = "+n.getBegin().get().in);


                                                // ce.replace(n, variableDeclarator);
                                               // return null;
                                            }
                                            return null;
                                        }
                                    }, null);*//*


                                }
                            });*/
                        }

                    }
                }


            });
        });






    }
    static ExpressionStmt createAndGetVariable(String variableName, String variableType, String varaibleInitializer){
        ExpressionStmt expressionStmt = new ExpressionStmt();
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        VariableDeclarator variableDeclarator = new VariableDeclarator();
        variableDeclarator.setName(variableName);
        variableDeclarator.setType(new ClassOrInterfaceType(variableType));
        variableDeclarator.setInitializer(varaibleInitializer);
        NodeList<VariableDeclarator> variableDeclarators = new NodeList<>();
        variableDeclarators.add(variableDeclarator);
        variableDeclarationExpr.setVariables(variableDeclarators);
        expressionStmt.setExpression(variableDeclarationExpr);
        return expressionStmt;
    }


}

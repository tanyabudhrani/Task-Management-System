package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

class CompositeCriterion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String logicOp;
    private List<CompositeCriterion> subCriteria;
    private CompositeCriterion negatedCriterion;

    public CompositeCriterion(String name) {
        this.name = name;
        this.subCriteria = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public String getLogicOp() {
        return logicOp;
    }

    public List<CompositeCriterion> getSubCriteria() {
        return subCriteria;
    }

    public CompositeCriterion getNegatedCriterion() {
        return negatedCriterion;
    }

    public void setLogicOp(String logicOp) {
        this.logicOp = logicOp;
    }

    public void setSubCriteria(List<CompositeCriterion> list) {
        this.subCriteria = list;
    }

    public void setNegatedCriterion(CompositeCriterion negatedCriterion) {
        this.negatedCriterion = negatedCriterion;
    }

    public void setName(String name) {
        this.name = name;
    }
}
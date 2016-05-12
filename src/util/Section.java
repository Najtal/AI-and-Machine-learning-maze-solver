package util;

import ucc.NodeDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jvdur on 12/05/2016.
 */
public class Section implements Comparable<Section>, Comparator<Section> {

    private int id;
    private boolean freeOfEvent;
    private boolean onPath;
    private int length;
    private NodeDTO startNode;
    private NodeDTO deadEndNode;

    private Section fatherSection;
    private List<Section> sonSection;
    private int progeniesSections;
    private int progeniesLength;

    /**
     * Constructor
     * @param id
     * @param onPath
     * @param fatherSection
     * @param startNode
     */
    public Section(int id, boolean onPath, Section fatherSection, NodeDTO startNode) {
        this.id = id;
        this.freeOfEvent = true;
        this.length = -1;
        this.onPath = onPath;
        this.sonSection = new ArrayList<>();
        this.fatherSection = fatherSection;
        this.startNode = startNode;
        this.setFreeOfEvent(true);
    }

    /**
     * Add a son section to this section
     * @param son
     */
    public void addSon(Section son) {
        sonSection.add(son);

        progeniesSections += son.getProgeniesSections();
        progeniesLength += son.getProgeniesLength();

    }


    public int getId() {
        return id;
    }

    public boolean isFreeOfEvent() {
        return freeOfEvent;
    }

    public void setFreeOfEvent(boolean freeOfEvent) {
        this.freeOfEvent = freeOfEvent;
    }

    public boolean isOnPath() {
        return onPath;
    }

    public void setOnPath(boolean onPath) {
        this.onPath = onPath;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public NodeDTO getStartNode() {
        return startNode;
    }

    public void setStartNode(NodeDTO startNode) {
        this.startNode = startNode;
    }

    public NodeDTO getDeadEndNode() {
        return deadEndNode;
    }

    public void setDeadEndNode(NodeDTO deadEndNode) {
        this.deadEndNode = deadEndNode;
    }

    public Section getFatherSection() {
        return fatherSection;
    }

    public void setFatherSection(Section fatherSection) {
        this.fatherSection = fatherSection;
    }

    public List<Section> getSonSection() {
        return sonSection;
    }

    public void setSonSection(List<Section> sonSection) {
        this.sonSection = sonSection;
    }

    public int getProgeniesSections() {
        return progeniesSections;
    }

    public void setProgeniesSections(int progeniesSections) {
        this.progeniesSections = progeniesSections;
    }

    public int getProgeniesLength() {
        return progeniesLength;
    }

    public void setProgeniesLength(int progeniesLength) {
        this.progeniesLength = progeniesLength;
    }

    @Override
    public int compareTo(Section o) {
        return this.getLength() - o.getLength();
    }

    @Override
    public int compare(Section o1, Section o2) {
        return o1.getLength() - o2.getLength();
    }
}

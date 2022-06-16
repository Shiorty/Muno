package at.htlkaindorf.ahif18.data;

/**
 * Is used to get a sequence of repeating numbers.
 *
 * Last changed: 2022-06-13
 * @author Jan Mandl
 */
public class LoopingSequence {
    /** contains the current value of the sequence **/
    private int sequentialNumber;
    /**
     * The first value of the LoopingSequence
     */
    private int start;
    /**
     * The value at which the LoopingSequence should start from the beginning
     */
    private int end;

    /**
     * Creates a new Sequence starting with the value 0.
     *
     * @param startFrom the first value of the LoopingSequence that is given out.
     * @param loopAt the last value that is being returned plus one
     */
    public LoopingSequence(int startFrom, int loopAt){
        start = startFrom;
        end = loopAt;
        this.sequentialNumber = startFrom - 1;
    }

    /**
     * Creates a new Sequence starting with the value 0.
     *
     * @param startFrom the first value of the LoopingSequence that is given out.
     * @param loopAt the last value that is being returned plus one
     * @param firstStart the value the LoopingSequence starts at after being constructed
     */
    public LoopingSequence(int startFrom, int loopAt, int firstStart){
        start = startFrom;
        end = loopAt;
        this.sequentialNumber = firstStart;
    }

    /**
     * Returns the next value in the sequence and loops if the value reaches the end
     * @return int representing the next value in the sequence
     */
    public synchronized int nextValue(){
        if(++sequentialNumber >= end) {
            sequentialNumber = start;
        }
        return sequentialNumber;
    }

    /**
     * Gets the current value of the sequence
     * @return the current value
     */
    public synchronized int currentValue(){
        return sequentialNumber;
    }

    /**
     * @return The highest value the LoopingSequence can return+1
     */
    public int getEnd() {
        return end;
    }

    public String toString() {
        return "start: " + start + " end: " + end + " sequentialNumber: " + sequentialNumber;
    }
}

package gsao64;


import gsao64.exceptions.*;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

public class GSSplitterBuffer
{
    public List<GSBuffer> mData;
    private int mCurrentBufferIndex;
    private int mMaxChan;
    private int mMaxTP;
    private int mMaxTPPerGSBuffer;

    public GSSplitterBuffer(int maxTP, int maxChan) throws BufferTooLargeException, BoardInitializeException
    {
        // digest parameters
        this.mMaxTP = maxTP;
        this.mMaxChan = maxChan;

        // Initialize the list
        this.mData = new LinkedList<>();
        mCurrentBufferIndex = 0;

        mMaxTPPerGSBuffer = (int)Math.floor(65536.0 / mMaxChan);
        int totalTP = mMaxTP;

        while (totalTP > 0) {
            this.mData.add(new GSBuffer(mMaxTPPerGSBuffer, mMaxChan));
            totalTP -= mMaxTPPerGSBuffer;
        }
        this.mData.add(new GSBuffer(mMaxTPPerGSBuffer, mMaxChan));
    }

    public void appendValue(double value, int i)
    {
        int totalValsWritten = this.mData.get(mCurrentBufferIndex).getValsWritten();
        if (mMaxTPPerGSBuffer * mMaxChan - totalValsWritten < mMaxChan)
            mCurrentBufferIndex++;

        try {
            this.mData.get(mCurrentBufferIndex).appendValue(value, i);
        } catch (ActiveChanException | VoltageRangeException e) {
            e.printStackTrace();
        }
    }

    public void appendEndofTP()
    {
        try {
            this.mData.get(mCurrentBufferIndex).appendEndofTP();
        } catch (FlagException e) {
            e.printStackTrace();
        }
    }

    public void appendEndofFunction()
    {
        try {
            this.mData.get(mCurrentBufferIndex).appendEndofFunction();
        } catch (FlagException e) {
            e.printStackTrace();
        }
    }

    public ArrayDeque<GSBuffer> getData() { return new ArrayDeque<>(this.mData); }

    public int getmCurrentBufferIndex() { return mCurrentBufferIndex; }
}

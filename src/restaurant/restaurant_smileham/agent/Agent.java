package restaurant.restaurant_smileham.agent;

import java.util.concurrent.Semaphore;

import restaurant.restaurant_smileham.test.mock.EventLog;

/**
 * Base class for simple agents
 */
public abstract class Agent {
	EventLog log = new EventLog();
    Semaphore stateChange = new Semaphore(1, true);//binary semaphore, fair
    private AgentThread agentThread;
    
    private Semaphore semPaused = new Semaphore(0);
    private boolean mPaused = false;

    protected Agent() {
    }

    /**
     * This should be called whenever state has changed that might cause
     * the agent to do something.
     */
    protected void stateChanged() {
        stateChange.release();
    }

    /**
     * Agents must implement this scheduler to perform any actions appropriate for the
     * current state.  Will be called whenever a state change has occurred,
     * and will be called repeated as long as it returns true.
     *
     * @return true iff some action was executed that might have changed the
     *         state.
     */
    protected abstract boolean pickAndExecuteAnAction();

    /**
     * Return agent name for messages.  Default is to return java instance
     * name.
     */
    protected String getName() {
        return StringUtil.shortName(this);
    }

    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(toString());
        sb.append(" ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }

    /**
     * Start agent scheduler thread.  Should be called once at init time.
     */
    public synchronized void startThread() {
        if (agentThread == null) {
            agentThread = new AgentThread(getName());
            agentThread.start(); // causes the run method to execute in the AgentThread below
        } else {
            agentThread.interrupt();//don't worry about this for now
        }
    }

    /**
     * Stop agent scheduler thread.
     */
    //In this implementation, nothing calls stopThread().
    //When we have a user interface to agents, this can be called.
    public void stopThread() {
        if (agentThread != null) {
            agentThread.stopAgent();
            agentThread = null;
        }
    }

    /**
     * Agent scheduler thread, calls respondToStateChange() whenever a state
     * change has been signalled.
     */
    private class AgentThread extends Thread {
        private volatile boolean goOn = false;

        private AgentThread(String name) {
            super(name);
        }

        public void run() {
            goOn = true;

            while (goOn) {
                try {
                	if (mPaused) semPaused.acquire();
                	
                    // The agent sleeps here until someone calls, stateChanged(),
                    // which causes a call to stateChange.give(), which wakes up agent.
                    stateChange.acquire();
                    //The next while clause is the key to the control flow.
                    //When the agent wakes up it will call respondToStateChange()
                    //repeatedly until it returns FALSE.
                    //You will see that pickAndExecuteAnAction() is the agent scheduler.
                    while (pickAndExecuteAnAction()) ;
                } catch (InterruptedException e) {
                    // no action - expected when stopping or when deadline changed
                } catch (Exception e) {
                    print("Unexpected exception caught in Agent thread:", e);
                }
            }
        }
        

        private void stopAgent() {
            goOn = false;
            this.interrupt();
        }
    }
    
    
    

    public void pause(){
    	mPaused = true;
    }
    
    public void restart(){
    	mPaused = false;
    	semPaused.release();
    }
    
    public boolean isPaused(){
    	return mPaused;
    }
}


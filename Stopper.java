class Stopper extends Thread{
	
	long stopTimeMillis;
	CommercialKiller parent;
	
	Stopper(CommercialKiller parent, long stopIn){
		this.parent = parent;
        stopTimeMillis = System.currentTimeMillis() + stopIn;
	}
	
	public void run(){
       
		while (System.currentTimeMillis() < this.stopTimeMillis)
			try {
				sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		parent.shouldStop = true;
	}
}
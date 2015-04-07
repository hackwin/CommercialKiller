import java.util.Arrays;

class ByteArrayQueue{

	byte[] queue;
	int position = 0;
	
	ByteArrayQueue(int length){
		queue = new byte[length];
	}
	
	void add(byte[] data){

		int spaceLeft = queue.length-position;
		
		if (spaceLeft >= data.length){ // read data into buffer until buffer is full
			System.arraycopy(data,0,queue,position,data.length);
			position += data.length;
		}
		else if (data.length > spaceLeft){ // add as much data to the buffer as possible 
			
			for(int i=0; i<spaceLeft; i++)
				queue[position+i] = data[i];
			
			data = Arrays.copyOfRange(data, spaceLeft, data.length); // save the rest of the data that didn't fit  
			position = queue.length;
		}
		
		if (queue.length == position && data.length > 0){  // process any data that doesn't fit 
			
			// shift array left
			System.arraycopy(queue, data.length, queue, 0, queue.length-data.length); //object src, src start, object dst, dst start, dst length

			// replace end of array with new data
			for(int i=0; i<data.length; i++)
				queue[queue.length-i-1] = data[data.length-1-i];
		}
		
	}
		
	public String toString(){
		String output = "";
		
		for(int i=0; i<queue.length; i++)
			output += queue[i] + ", ";
		
		return "["+output.substring(0, output.length()-2)+"]";
	}

}

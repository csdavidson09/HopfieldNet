import java.util.*;
import java.io.*;

/**
----------------------------------
----------------------------------
A program implementing a
hopfield net. Classifies various
pictures.
Author: Christian Davidson
Date: November 6, 2011
Neural Networks CS380
----------------------------------
----------------------------------
*/

public class Hopfield
{
	public static void main(String[] args)
	{
		String kill = "";
		String ans = "p";
		String inputName = "";
		String outputName = "";
		int[][] weights = new int[0][0];
		boolean trained = false;
		int numtest = 0, dimcol = 0, dimrow = 0, dim = 0;
		
		Scanner kb = new Scanner(System.in);
		
		System.out.println("Welcome to my Hopfield Net");
		while(ans != "q")
		{
//Printing out menu
			System.out.println("");
			System.out.println("Enter one of the following:");
			System.out.println("'c' - To train the net");
			System.out.println("'h' - To load some weights");
			System.out.println("'r' - To save current weights");
			System.out.println("'i' - To test the net");
			System.out.println("'s' - To quit the net");
			ans = kb.next();

//Option "c"
//Train the neural net
			if(ans.equalsIgnoreCase("c"))
			{
				System.out.println("");
				System.out.println("Enter the Training Data file name:");
				inputName = kb.next();
				int[][] tempWeights = new int[0][0];
				try
				{
//Reading in data from training
//data file
					Scanner in = new Scanner(new File(inputName));
					dimrow = in.nextInt();
					dimcol = in.nextInt();
					numtest = in.nextInt();
					dim = dimrow * dimcol;
					kill = in.nextLine();
					tempWeights = new int[numtest][dim];
					for(int count = 0; count < numtest; count++)
					{
						kill = in.nextLine();
						int tempIndex = 0;
						for(int j = 0; j < dimcol; j++)
						{
							String tempString = in.nextLine();
							for(int i = 0; i < dimrow; i++)
							{
								char tempChar = tempString.charAt(i);
								if(tempChar == ' ')
									tempWeights[count][tempIndex] = -1;
								else
									tempWeights[count][tempIndex] = 1;
								tempIndex++;
							}
						}
					}
					in.close();
				}
				catch(FileNotFoundException e)
				{
					System.out.println(e.getMessage());
					System.exit(1);
				}
				catch(NoSuchElementException e)
				{
					System.out.println(e.getMessage());
					System.exit(1);
				}			
				weights = new int[dim][dim];
				for(int j = 0; j < dim; j++)
				{
					for(int i = 0; i < dim; i++)
						weights[j][i] = 0;
				}
//tempWeights1 is our x input
//tempWeights2 is our transpose
//and tempWeihgts3 is a matrix
//used to store their dot product
				int[][] tempWeights1 = new int[1][dim];
				int[][] tempWeights2 = new int[dim][1];
				int[][] tempWeights3 = new int[dim][dim];
				for(int count = 0; count < numtest; count++)
				{
					for(int i = 0; i < dim; i++)
					{
						tempWeights1[0][i] = tempWeights[count][i];
						tempWeights2[i][0] = tempWeights[count][i];
					}
					tempWeights3 = multiplyMatrices(tempWeights2, tempWeights1);
					weights = addMatrices(weights, tempWeights3);
				}					
				System.out.println("");
				System.out.println("The net has been successfully trained");
				trained = true;
			}

//Option "h"
//Load in weights			
			else if(ans.equalsIgnoreCase("h"))
			{
				System.out.println("");
				System.out.println("Enter the name of the weights file:");
				inputName = kb.next();
				try
				{
//Reading in data from the
//given weights file
					Scanner in = new Scanner(new File(inputName));
					dimrow = in.nextInt();
					dimcol = in.nextInt();
					dim = dimrow * dimcol;
					weights = new int[dim][dim];
					for(int j = 0; j < dim; j++)
					{
						for(int i = 0; i < dim; i++)
							weights[j][i] = in.nextInt();
					}
					in.close();
				}
				catch(FileNotFoundException e)
				{
					System.out.println(e.getMessage());
					System.exit(1);
				}
				System.out.println("");
				System.out.println("Weights successfully loaded");
				trained = true;
			}

//Option "r"
//Save weights
			else if(ans.equalsIgnoreCase("r"))
			{
				if(trained == true)
				{
					System.out.println("");
					System.out.println("Enter the weights output file name:");
					outputName = kb.next();
					try
					{
//Saving out weights data to the
//specified file
						PrintWriter out = new PrintWriter(outputName);
						out.println(dimrow);
						out.println(dimcol);
						for(int j = 0; j < dim; j++)
						{
							for(int i = 0; i < dim; i++)
							{
								out.print(weights[j][i] + " ");
							}
							
							out.println("");
						}
						out.close();
					}
					catch(IOException e)
					{
						System.out.println(e);
						System.exit(1);
					}							
					System.out.println("");
					System.out.println("Weights have been successfully saved into the file: " + outputName);
				}
//user does not have weights
//to save to a file
                                else
                                {
                                        System.out.println("");
                                        System.out.println("You need to train the net first.");
                                }
			
			}

//Option "i"
//Test the net
			else if(ans.equalsIgnoreCase("i"))
			{
//Attempt to train
//with weights
//uploaded
				if(trained == true)
				{
					System.out.println("");
					System.out.println("What is the name of the file you wish to test:");
					inputName = kb.next();
					int[][] testingWeights = new int[0][0];
					try
                                	{
//Reading in data
//from the given testing file
                                        	Scanner in = new Scanner(new File(inputName));
                                        	dimrow = in.nextInt();
                                        	dimcol = in.nextInt();
                                        	numtest = in.nextInt();
                                        	dim = dimrow * dimcol;
                                        	kill = in.nextLine();
                                        	testingWeights = new int[numtest][dim];
                                       	 	for(int count = 0; count < numtest; count++)
                                        	{
                                                	kill = in.nextLine();
                                                	int tempIndex = 0;
                                                	for(int j = 0; j < dimcol; j++)
                                                	{
                                                        	String tempString = in.nextLine();
                                                        	for(int i = 0; i < dimrow; i++)
                                                        	{   
                                                        	        char tempChar = tempString.charAt(i);
                                                                	if(tempChar == ' ')
                                                                        	testingWeights[count][tempIndex] = -1;
                                                                	else
                                                                       		testingWeights[count][tempIndex] = 1;
                                                                	tempIndex++;
                                                        	}
                                                	}
                                        	}
                                        	in.close();
                                	}
                                	catch(FileNotFoundException e)
                                	{
                                       		System.out.println(e.getMessage());
                                        	System.exit(1);
                                	}
                                	catch(NoSuchElementException e)
                                	{
                                       		System.out.println(e.getMessage());
                                        	System.exit(1);
                                	}
//initializing varibles used in
//our testing algorithm
					int[] randomUpdate = new int[dim];
					boolean converged = false;
					double yin = 0;
					int y = 0;
					int epochs = 0;
//copying our array for
//later printing of results
					int[][] origTesting = new int[numtest][dim];
					for(int count = 0; count < numtest; count++)
					{
						for(int i = 0; i < dim; i++)
							origTesting[count][i] = testingWeights[count][i];
					}
//Testing algorithm
					while(!converged)
					{
						epochs++;
						converged = true;
//for each pattern
						for(int count = 0; count < numtest; count++)
						{
//creating a random permutation array
							for(int k = 0; k < dim; k++)
								randomUpdate[k] = k;
							randomUpdate = randomPermutation(randomUpdate);
//for each neuron
							for(int k = 0; k < dim; k++)
							{
								yin = testingWeights[count][randomUpdate[k]];
								for(int i = 0; i < dim; i++)
									yin += testingWeights[count][i] * weights[randomUpdate[k]][i];								
//activation function
								if(yin > 0)
									y = 1;
								else if(yin < 0)
									y = -1;
//if change in activation, testing is not converged
//and update our array
								if(y != testingWeights[count][randomUpdate[k]])
								{
									converged = false;
									testingWeights[count][randomUpdate[k]] = y;
								}
							}
						}
					}
					System.out.println("");
					System.out.println("Training converged after " + epochs + " epochs.");

//Printing out the results of
//testing into a file
					System.out.println("What is the name of the file you would like to print results to:");
					outputName = kb.next();
					try
					{
						PrintWriter out = new PrintWriter(outputName);
						int counter = 0;
						int counter2 = 0;
						for(int count = 0; count < numtest; count++)
						{
                                                        out.println("PATTERN " + (count + 1) + ":");
                                                        out.println("");
//counters for the columns of our arrays
							counter = 0;
							counter2 = 0;
//loop used for each column of our output pattern
							for(int j = 0; j < dimcol; j++)
							{
//printing out row information of the input testing file
								for(int i = 0; i < dimrow; i++)
								{
									if(origTesting[count][counter] == 1)
										out.print("0");
									else
										out.print(" ");
									counter++;
								}
								out.print("          ->          ");
//printing out row information of the output
//obtained through testing
								for(int i = 0; i < dimrow; i++)
								{
									if(testingWeights[count][counter2] == 1)
										out.print("0");
									else
										out.print(" ");
									counter2++;
								}
								out.println("");
							}
							out.println("");
							out.println("-------------------------");
							out.println("");
						}
						out.close();
					}
					catch (IOException e)
					{
						System.out.println(e.getMessage());
						System.exit(1);
					}
					System.out.println("");
//Successfully trained and
//printed results to file
					System.out.println("Successfully trained file: " + inputName);
					System.out.println("Successfully printed to file: " + outputName);
				}
//Attempt to train
//without having
//weights
				else
				{
					System.out.println("");
					System.out.println("You need to train the net, or load in weights before testing.");
				}
			}

//Option "s"
//Quit the net
			else if(ans.equalsIgnoreCase("s"))
			{
				System.out.println("");
				System.out.println("Thank you for using my Hopfield Neural Net.");
				System.out.println("");
				System.exit(1);
			}

//No Option
//Invalid input, repeat
			else
			{
				System.out.println("");
				System.out.println("You have entered an invalid input.");
			}
		}
	}


//Method to add
//two Matrices

	public static int[][] addMatrices(int[][] a, int[][] b)
	{
		int aRows = a.length, aColumns = a[0].length, 
			bRows = b.length, bColumns = b[0].length;

		if (aRows != bRows || aColumns != bColumns)
			throw new IllegalArgumentException("Incompatible for addition");

		int[][] sum = new int[aRows][bColumns];
		
		for (int i = 0; i < aRows; i++)
		{
			for (int j = 0; j < aColumns; j++)
				sum[i][j] = a[i][j] + b[i][j];
		}

		return sum;
	}

//Method to multiply
//two Matrices
	
	public static int[][] multiplyMatrices(int[][] a, int[][] b)
	{
		int aRows = a.length, aColumns = a[0].length, 
			bRows = b.length, bColumns = b[0].length;

		if (aColumns != bRows)
			throw new IllegalArgumentException("Incompatible for multiplication");

		int[][] product = new int[aRows][bColumns];

		for (int i = 0; i < aRows; i++)
		{
			for (int j = 0; j < bColumns; j++)
			{
				if (i == j)
					product[i][j] = 0;
				else
				{
					for (int k = 0; k < aColumns; k++)
						product[i][j] += a[i][k] * b[k][j];
				}
			}
		}

		return product;
	}
	
//Method to
//Randomly permutate
//an array
	public static int[] randomPermutation(int[] a)
	{
		Random generator = new Random();
		for(int i = 0; i < a.length; i++)
		{
			int r = generator.nextInt(a.length);
			int temp = a[i];
			a[i] = a[r];
			a[r] = temp;
		}
		return a;
	}
}		


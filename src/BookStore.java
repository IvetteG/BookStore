import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;

public class BookStore 
{	
	
	private static final int OK = 0;
	private static final int NOT_IN_STOCK = 1;
	private static final int DOES_NOT_EXIST = 2;
	private static final int NO_ITEMS_IN_CART = 10;
	private static final int ITEMS_IN_CART = 11;

	public static void main(String[] args) 
	{
		URL url;
		String inputLine;
		CustomerCart customerCart = new CustomerCart();
		try
		{
			InputStreamReader inputStream = new InputStreamReader(System.in);
			BufferedReader input = new BufferedReader(inputStream);
		
			url =  new URL("http://www.contribe.se/bookstoredata/bookstoredata.txt");
			BookList booksInStore = new BookStoreList();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			while ((inputLine=in.readLine())!= null)
			{
				String []splitText = inputLine.split(";");
				Book book = new Book();
				String title = new String(splitText[0]);
				String author = new String(splitText[1]);
				book.setTitle(title); //Title
				book.setAuthor(author); //Author
				String str = splitText[2].replaceAll(",","");
				book.setPrice(new BigDecimal(str)); //Price
				booksInStore.add(book, Integer.parseInt(splitText[3])); //Amount
			}
			in.close();
			
			
			int customerChoice = 0;

			boolean programEnds = false;
			
			do
			{
				PrintMenu();
				try
				{
					customerChoice = Integer.parseInt(input.readLine());
				}catch(NumberFormatException ex)
				{
					//Print out in switch default
				}
				switch(customerChoice)
				{
					case 1: ShowAllBooks(booksInStore);
							Book bookList[] = AllBooks(booksInStore);
							AddBooksToCart(input, bookList, customerCart);
						break;
					case 2: Book searchList[] = Search(input,booksInStore);
							AddBooksToCart(input, searchList, customerCart);
						break;
					case 3: System.out.println("--------------------------------");
							if(PrintCustomerCart(customerCart)==ITEMS_IN_CART)
							{
								ChangeCart(input, customerCart, booksInStore);
							}
						break;
					case 4: System.out.println("The program ends.");
							programEnds = true;
							
						break;
					default: System.out.println("Choose a number between 1-4");
						break;
				}
				customerChoice = 0;
				//PrintMenu();
			}
			while(!programEnds);
			System.out.println("*************Thanks for shopping!*****************");
			input.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------
	private static void AddBooksToCart(BufferedReader input, Book bookList[], CustomerCart customerCart) 
	{
		String buyBooks = "";
		try 
		{
			do
			{
				buyBooks = "";
				System.out.println("Do you want to add any books to your cart? Yes(y) or No(n) then enter");
				buyBooks = input.readLine().toLowerCase();
				if(buyBooks.matches("y"))
				{
					try
					{
					System.out.println("Press the number of the book and then the amounts that you want to add into your cart.");
					System.out.println("Booknumber: ");
					
					int bookNumber = Integer.parseInt(input.readLine());
					if(bookNumber <= bookList.length && bookNumber > 0)
					{
						System.out.println("The amount:");
						int amount = Integer.parseInt(input.readLine());
						Book tmp = new Book();
						tmp = bookList[(bookNumber-1)];
						customerCart.AddToCart(tmp, amount);
						PrintCustomerCart(customerCart);
					}
					else
						System.out.println("Choose a book between 1-" + bookList.length );
					}catch(NumberFormatException ex)
					{
						System.out.println("Choose a number");
					}
				}
				else
				{
					if(buyBooks.matches("n") != true)
						System.out.println("Wrong input try again.");
				}
			}while(!buyBooks.matches("n"));
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	private static void ChangeCart(BufferedReader input, CustomerCart customerCart, BookList booksInStore)
	{
		System.out.println("Want to change something in your cart? Yes(y) No(n) or do you want to buy the items in your cart? Buy(b)");
		String changeCart="";
		try {
			changeCart = input.readLine().toLowerCase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(changeCart.matches("y"))
		{
			ChangeCartMenu(input, customerCart, booksInStore);
		}
		else if(changeCart.matches("b"))
		{
			int listBooksOkToBuy[] = BuyBooksCheck(input, customerCart, booksInStore);
			BuyBooks(input, customerCart, listBooksOkToBuy, booksInStore);
		}
		else if(changeCart.matches("n"))
		{
			
		}
		else
		{
			System.out.println("wrong input - try again");
			ChangeCart(input, customerCart, booksInStore);
		}
	}
	//---------------------------------------------------------------------------------------------------------
	private static void ChangeCartMenu(BufferedReader in, CustomerCart customerCart, BookList booksInStore) 
	{
		System.out.println("**************************************************************");
		
		try 
		{
			if(customerCart == null)
			{
				System.out.println("Your cart is empty!!!");
			}
			else if(customerCart != null)
			{
				int change = 0;
				boolean finished = false;
				Book[] cart = customerCart.BooksInCart();	;
				do
				{	
						System.out.println("Do you want to: \n1. Delete a book in cart   2. Change amount of book  3. Buy all the books in the cart  4. Go back to Main Menu");
						try
						{
							change = Integer.parseInt(in.readLine());
						}catch(NumberFormatException ex)
						{
							//wrong input
						}
						
						switch(change)
						{
							case 1: if(PrintCustomerCart(customerCart) == ITEMS_IN_CART)
									{
										System.out.println("Whitch book do you want to delete?");
										try
										{
											change = Integer.parseInt(in.readLine());
											if(change >= 1 && change <= cart.length)
											{
												customerCart.EraseFromCart(cart[change-1], 1/*amount[change-1]*/);
											}
										}catch(NumberFormatException ex)
										{
											System.out.println("Wrong input try again");
										}
									}
								break;
							case 2: if(PrintCustomerCart(customerCart) == ITEMS_IN_CART)
									{
										System.out.println("Whitch book do you want to change the amount of?");
										try
										{
											change = Integer.parseInt(in.readLine());
											
											if(change >= 1 && change <= cart.length)
											{
												System.out.println("How many do you want to add or substract?");
												int amountChange = Integer.parseInt(in.readLine());
												customerCart.AddToCart(cart[change-1], amountChange);
											}
											else
											{
												System.out.println("Wrong input");
											}
										}catch(NumberFormatException ex)
										{
											System.out.println("Wrong input try again");
										}
									}
								break;
							case 3: if(PrintCustomerCart(customerCart) == ITEMS_IN_CART)
									{
										int listBooksOkToBuy[] = BuyBooksCheck(in, customerCart, booksInStore);
										BuyBooks(in, customerCart, listBooksOkToBuy, booksInStore);
									}
								break;
							case 4: finished =true;
								break;
							default: System.out.println("Wrong input, choose only a number between 1-4");
								break;
							
						}
					
					}while(!finished);
				}
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	//---------------------------------------------------------------------------------------------------------
	public static int[] BuyBooksCheck(BufferedReader in,CustomerCart customerCart, BookList booksInStore)
	{
		Book[] booksCustomer = customerCart.BooksInCart();
		
		int[] buyingBooks = booksInStore.buy(booksCustomer);
		
		for(int i= 0; i < booksCustomer.length; ++i)
		{
			if(buyingBooks[i] == OK)
			{
				System.out.println("Book that is ok to buy :");
				ShowBookInfo(booksCustomer[i],i);
			}
			else if (buyingBooks[i] == NOT_IN_STOCK)
			{
				System.out.println("Book that is not in stock :");
				ShowBookInfo(booksCustomer[i],i);
			}
			else
			{
				System.out.println("Book that don't exist in our stock :");
				ShowBookInfo(booksCustomer[i],i);
			}
		}
		return buyingBooks;
	}
	//---------------------------------------------------------------------------------------------------------
	private static void BuyBooks(BufferedReader in, CustomerCart customerCart, int[] buyingBooks, BookList booksInStore) 
	{
		String input = "";
		try
		{
			System.out.println("Do you want to buy the books that are in stock? Yes(y) or No(n)");
			
			input = in.readLine().toLowerCase();
			System.out.println(input);
			BigDecimal price = new BigDecimal(0);
			Book bookList[] = booksInStore.ListAllBooks();
			if(input.matches("y"))
			{
				Book buyBooks[] = new Book[buyingBooks.length]; 
				buyBooks = customerCart.BooksInCart();
				for(int i = 0; i < buyBooks.length;++i)
				{
					if(buyingBooks[i] == OK)
					{
						for(int j = 0; j < buyBooks[i].getAmount(); ++j)
						{
							price = price.add(buyBooks[i].getPrice());
						}
					}
				}
				for(int i=0; i< buyBooks.length;++i)
				{
					if(buyingBooks[i] == OK)
					{
						for(int j=0; j< bookList.length; ++j)
						{
							if(bookList[j].getTitle().matches(buyBooks[i].getTitle()) && bookList[j].getAuthor().matches(buyBooks[i].getAuthor()))
							{
								bookList[j].setAmount(bookList[j].getAmount() - buyBooks[i].getAmount());
							}
						}
						customerCart.EraseFromCart(buyBooks[i], buyBooks[i].getAmount());
					}
				}
				System.out.println("Total price: " + price);
			}
			else if(input.matches("n"))
			{
				//PrintMenu();
			}
			else
			{
				System.out.println("wrong input");
				BuyBooks(in, customerCart, buyingBooks, booksInStore);
			}

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------------------------------------
	public static int PrintCustomerCart(CustomerCart customerCart)
	{
		Book[] cart = customerCart.BooksInCart();
		if(cart == null)
		{
			System.out.println("Your cart is empty!");
			return NO_ITEMS_IN_CART;
		}
		else
		{
			System.out.println("****************************************");
			for(int i=0; i < cart.length; i++)
			{
				System.out.println("Number: " + (i+1)+ 
									" Title: '" + cart[i].getTitle() + 
									"' Author: '"+cart[i].getAuthor() + 
									"' Price: " + cart[i].getPrice() +
									" Amount: " + cart[i].getAmount());
				System.out.println("-------------------------------------");
			}
			return ITEMS_IN_CART;
		}
	}
	//---------------------------------------------------------------------------------------------------------
	public static void PrintMenu()
	{
		System.out.println("***********************************");
		System.out.println("Welcome to our bookstore!");
		System.out.println("1. Press one for our entire list of our books");
		System.out.println("2. Press two for searching for a title or an author");
		System.out.println("3. Press three for show your cart");
		System.out.println("4. Exit");
	}
	//---------------------------------------------------------------------------------------------------------
	public static void ShowAllBooks(BookList bookList)
	{
		System.out.println("******************************************");
		Book[] list= bookList.ListAllBooks();
		
		int count =0;
		while(count < list.length)
		{
			ShowBookInfo(list[count], count);
			count++;
		}
	}
	//---------------------------------------------------------------------------------------------------------
	public static Book[] AllBooks(BookList bookList)
	{
		Book[] list = bookList.ListAllBooks();
		return list;
	}
	//---------------------------------------------------------------------------------------------------------
	public static Book[] Search(BufferedReader input, BookList bookList)
	{
		String searchString;
		try 
		{
			System.out.println("Write the title of the book or the authors name: ");
			searchString = input.readLine().toLowerCase();
			
			Book book[] = bookList.list(searchString);
			int count = 0;

			if(book == null)
			{
				System.out.println("No author/title with the name: " +searchString);
				System.out.println("Press 1 if you want to try again, press 2 for exit to main menu");
				String choice = input.readLine();
				if(choice.matches("1") == true)
					Search(input, bookList);
				else if(choice.matches("2") == true)
				{
					
				}
				else
				{
					System.out.println("Wrong input");
					Search(input, bookList);
				}
			}
			else
			{
				
				System.out.println("Found: " + book.length + " books: ");
				Book searchList[] = new Book[book.length];
				count = 0;
				while(count < book.length)
				{
					searchList[count] = book[count];
					//ShowBookInfo(searchList[count], count, amount);
					System.out.println("Number: " + (count+1) + " Title: '" + book[count].getTitle() + "' Author: '" + book[count].getAuthor() + "' Price: " + book[count].getPrice());
					System.out.println("--------------------------------------------------------------");
					count++;
				}
				return searchList;
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	//---------------------------------------------------------------------------------------------------------
	public static void ShowBookInfo(Book book, int index)
	{
		System.out.println("Number: "+ (index+1) + " Title: '" + book.getTitle() + "' Author: '" + book.getAuthor() + "' Price: " + book.getPrice() + " Amount in stock: "+ book.getAmount());
		System.out.println("-----------------------------------------------------------------");
	}
}
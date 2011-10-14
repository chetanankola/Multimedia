// Image.cpp : Defines the entry point for the application.
//

#include "Image.h"
#include <cmath>



MyImage myImage;
int printfile=0;  //global variables 
int clamping=1;   //clamps the YUV to RGB converted values after rounding them.
int THRESHOLD = 400;

#define MAX_LOADSTRING 100
#define IMGHEIGHT 288
#define IMGWIDTH 352
#define MAX_SUBSAMPLINGVALUE 8
#define round(a) ((a>0)?((int)(a+0.5)):((int)(a-0.5)))
/*#define ceil(a) ((int)(a-0.5))
#define floor(a) ((int)(a+0.5))
#define ceiling(a) ((int)a+
*/
// Global Variables:
HINSTANCE hInst;								// current instance
TCHAR szTitle[MAX_LOADSTRING];								// The title bar text
TCHAR szWindowClass[MAX_LOADSTRING];								// The title bar text

// Foward declarations of functions included in this code module:
ATOM				MyRegisterClass(HINSTANCE hInstance);
BOOL				InitInstance(HINSTANCE, int);
LRESULT CALLBACK	WndProc(HWND, UINT, WPARAM, LPARAM);
LRESULT CALLBACK	About(HWND, UINT, WPARAM, LPARAM);




//start main
int APIENTRY WinMain(HINSTANCE hInstance,
                     HINSTANCE hPrevInstance,
                     LPSTR     lpCmdLine,
                     int       nCmdShow)
{
 	// TODO: Place code here.
	MSG msg;
	HACCEL hAccelTable;

	//int y,u,v,q;   //Subsampling variables and quantization
	int n,m=0; //n is number of codebook vectors m is 0 or 1 depending on what algorithm is used
	char ImagePath[_MAX_PATH];
	//sscanf(lpCmdLine, "%s %d %d", &ImagePath, &w, &h);
	sscanf(lpCmdLine, "%s %d %d %d %d %d", &ImagePath,&n,&m,&THRESHOLD);
	myImage.setWidth(IMGWIDTH);
	myImage.setHeight(IMGHEIGHT);
	//myImage.setYUVQ(y,u,v,q);
	myImage.setcodebookandalgotype(n,m);
	myImage.setImagePath(ImagePath);
	myImage.ReadImage();
	myImage.initializeoutRGB();
	//myImage.ConvertRGBtoYUV();
	//myImage.SubsampleYUV();
	//myImage.UpsampleYUV();
	//myImage.ConvertYUVtoRGB();
	myImage.Create3dhistogram();
	

	

	if(m==2)
	{
		myImage.computesmartbitssplitforRGB();
		myImage.createsmartCodeBook();
		myImage.smartencodeImage();
	}
	else
	{
		myImage.computebitssplitforRGB();//step size is also computed by now
		myImage.createCodeBook();
		myImage.encodeImage(); //unsigned char*  encodedImage; will be filled with codebook index

	}

	myImage.decodeImage(); //will create a new image from the encoded image using codebook
	myImage.createchannelizedImage();
	//myImage.Quantize();




	// Initialize global strings
	LoadString(hInstance, IDS_APP_TITLE, szTitle, MAX_LOADSTRING);
	LoadString(hInstance, IDC_IMAGE, szWindowClass, MAX_LOADSTRING);
	MyRegisterClass(hInstance);

	// Perform application initialization:
	if (!InitInstance (hInstance, nCmdShow)) 
	{
		return FALSE;
	}

	hAccelTable = LoadAccelerators(hInstance, (LPCTSTR)IDC_IMAGE);

	// Main message loop:
	while (GetMessage(&msg, NULL, 0, 0)) 
	{
		if (!TranslateAccelerator(msg.hwnd, hAccelTable, &msg)) 
		{
			TranslateMessage(&msg);
			DispatchMessage(&msg);
		}
	}
	
	return msg.wParam;
}



//
//  FUNCTION: MyRegisterClass()
//
//  PURPOSE: Registers the window class.
//
//  COMMENTS:
//
//    This function and its usage is only necessary if you want this code
//    to be compatible with Win32 systems prior to the 'RegisterClassEx'
//    function that was added to Windows 95. It is important to call this function
//    so that the application will get 'well formed' small icons associated
//    with it.
//
ATOM MyRegisterClass(HINSTANCE hInstance)
{
	WNDCLASSEX wcex;

	wcex.cbSize = sizeof(WNDCLASSEX); 

	wcex.style			= CS_HREDRAW | CS_VREDRAW;
	wcex.lpfnWndProc	= (WNDPROC)WndProc;
	wcex.cbClsExtra		= 0;
	wcex.cbWndExtra		= 0;
	wcex.hInstance		= hInstance;
	wcex.hIcon			= LoadIcon(hInstance, (LPCTSTR)IDI_IMAGE);
	wcex.hCursor		= LoadCursor(NULL, IDC_ARROW);
	wcex.hbrBackground	= (HBRUSH)(COLOR_WINDOW+1);
	wcex.lpszMenuName	= (LPCSTR)IDC_IMAGE;
	wcex.lpszClassName	= szWindowClass;
	wcex.hIconSm		= LoadIcon(wcex.hInstance, (LPCTSTR)IDI_SMALL);

	return RegisterClassEx(&wcex);
}

//
//   FUNCTION: InitInstance(HANDLE, int)
//
//   PURPOSE: Saves instance handle and creates main window
//
//   COMMENTS:
//
//        In this function, we save the instance handle in a global variable and
//        create and display the main program window.
//
BOOL InitInstance(HINSTANCE hInstance, int nCmdShow)
{
   HWND hWnd;

   hInst = hInstance; // Store instance handle in our global variable

   hWnd = CreateWindow(szWindowClass, szTitle, WS_OVERLAPPEDWINDOW,
      CW_USEDEFAULT, 0, CW_USEDEFAULT, 0, NULL, NULL, hInstance, NULL);

   if (!hWnd)
   {
      return FALSE;
   }
   
   ShowWindow(hWnd, nCmdShow);
   UpdateWindow(hWnd);

   return TRUE;
}

//
//  FUNCTION: WndProc(HWND, unsigned, WORD, LONG)
//
//  PURPOSE:  Processes messages for the main window.
//
//  WM_COMMAND	- process the application menu
//  WM_PAINT	- Paint the main window
//  WM_DESTROY	- post a quit message and return
//
//
LRESULT CALLBACK WndProc(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam)
{
	int wmId, wmEvent;
	PAINTSTRUCT ps;
	HDC hdc;
	TCHAR szHello[MAX_LOADSTRING];
	LoadString(hInst, IDS_HELLO, szHello, MAX_LOADSTRING);

	switch (message) 
	{
		case WM_COMMAND:
			wmId    = LOWORD(wParam); 
			wmEvent = HIWORD(wParam); 
			// Parse the menu selections:
			switch (wmId)
			{
				case IDM_ABOUT:
				   DialogBox(hInst, (LPCTSTR)IDD_ABOUTBOX, hWnd, (DLGPROC)About);
				   break;
				case IDM_EXIT:
				   DestroyWindow(hWnd);
				   break;
				default:
				   return DefWindowProc(hWnd, message, wParam, lParam);
			}
			break;
		case WM_PAINT:
			{
				hdc = BeginPaint(hWnd, &ps);
				// TODO: Add any drawing code here...
				RECT rt;
				GetClientRect(hWnd, &rt);
				char text[1000];
				strcpy(text, "Original image and output image\n");
				DrawText(hdc, text, strlen(text), &rt, DT_LEFT);
				strcpy(text, "\n");
				DrawText(hdc, text, strlen(text), &rt, DT_LEFT);

/*				BITMAPINFO gmi;
				memset(&gmi,0,sizeof(gmi));
				gmi.bmiHeader.biSize = sizeof(gmi.bmiHeader);
				gmi.bmiHeader.biWidth = myImage.getWidth();
				gmi.bmiHeader.biHeight = -myImage.getHeight();  // Use negative height.  DIB is top-down.
				gmi.bmiHeader.biPlanes = 1;
				gmi.bmiHeader.biBitCount = 24;
				gmi.bmiHeader.biCompression = BI_RGB;
				gmi.bmiHeader.biSizeImage = myImage.getWidth()*myImage.getHeight();
				gmi.bmiHeader.biClrUsed=0;
*/




				BITMAPINFO bmi;
				CBitmap bitmap;
				memset(&bmi,0,sizeof(bmi));
				bmi.bmiHeader.biSize = sizeof(bmi.bmiHeader);
				bmi.bmiHeader.biWidth = myImage.getWidth();
				bmi.bmiHeader.biHeight = -myImage.getHeight();  // Use negative height.  DIB is top-down.
				bmi.bmiHeader.biPlanes = 1;
				bmi.bmiHeader.biBitCount = 24;
				bmi.bmiHeader.biCompression = BI_RGB;
				bmi.bmiHeader.biSizeImage = myImage.getWidth()*myImage.getHeight();
				

				SetDIBitsToDevice(hdc,
								  0,100,myImage.getWidth(),myImage.getHeight(),
								  0,0,0,myImage.getHeight(),
								  myImage.getImageData(),&bmi,DIB_RGB_COLORS);

				SetDIBitsToDevice(hdc,
								  myImage.getWidth()+50,100,myImage.getWidth(),myImage.getHeight(),
								  0,0,0,myImage.getHeight(),
								  myImage.getOutImageData(),&bmi,DIB_RGB_COLORS);



				//GRAY SCALE!!! DISPPLAY!

				SetDIBitsToDevice(hdc,
								  myImage.getWidth()*2+50,100,myImage.getWidth(),myImage.getHeight(),
								  0,0,0,myImage.getHeight(),
								  myImage.getrImage(),&bmi,DIB_RGB_COLORS);

				SetDIBitsToDevice(hdc,
								  0,110+myImage.getHeight(),myImage.getWidth(),myImage.getHeight(),
								  0,0,0,myImage.getHeight(),
								  myImage.getgImage(),&bmi,DIB_RGB_COLORS);

				SetDIBitsToDevice(hdc,
								  myImage.getWidth()+50,110+myImage.getHeight(),myImage.getWidth(),myImage.getHeight(),
								  0,0,0,myImage.getHeight(),
								  myImage.getbImage(),&bmi,DIB_RGB_COLORS);


				EndPaint(hWnd, &ps);
			}
			break;		
		case WM_DESTROY:
			PostQuitMessage(0);
			break;
		default:
			return DefWindowProc(hWnd, message, wParam, lParam);
   }
   return 0;
}

// Mesage handler for about box.
LRESULT CALLBACK About(HWND hDlg, UINT message, WPARAM wParam, LPARAM lParam)
{
	switch (message)
	{
		case WM_INITDIALOG:
				return TRUE;

		case WM_COMMAND:
			if (LOWORD(wParam) == IDOK || LOWORD(wParam) == IDCANCEL) 
			{
				EndDialog(hDlg, LOWORD(wParam));
				return TRUE;
			}
			break;
	}
    return FALSE;
}



// MyImage


void MyImage::ReadImage()
{

	FILE *IN_FILE;
	int i;	
	char *Rbuf = new char[Height*Width]; 
	char *Gbuf = new char[Height*Width]; 
	char *Bbuf = new char[Height*Width]; 

	IN_FILE = fopen(ImagePath, "rb");

	if (IN_FILE == NULL) 
	{
		fprintf(stderr, "Error");
		exit(0);
	}

	for (i = 0; i < Width*Height; i ++)
	{
		Rbuf[i] = fgetc(IN_FILE);
	}
	for (i = 0; i < Width*Height; i ++)
	{
		Gbuf[i] = fgetc(IN_FILE);
	}
	for (i = 0; i < Width*Height; i ++)
	{
		Bbuf[i] = fgetc(IN_FILE);
	}
	
	Data = new char[Width*Height*3];
	inRGBData = new unsigned char[Width*Height*3];
	for (i = 0; i < Height*Width; i++)
	{
		Data[3*i]	= Bbuf[i];
		Data[3*i+1]	= Gbuf[i];
		Data[3*i+2]	= Rbuf[i];
	}

	for (i = 0; i < Height*Width; i++)
	{
		inRGBData[3*i]		=   (unsigned char) Data[3*i];
		inRGBData[3*i+1]	=   (unsigned char) Data[3*i+1];
		inRGBData[3*i+2]	=   (unsigned char) Data[3*i+2];
	}

	delete [] Rbuf;
	delete [] Gbuf;
	delete [] Bbuf;

	fclose(IN_FILE);
	
}


void MyImage::Create3dhistogram()
{

	FILE *fd = fopen("histograminfo.txt","w");
	//initialize histo3d to zero
		for(int i=0;i<256;i++)
		{
				for(int j=0;j<256;j++)
				{
					for(int k=0;k<256;k++)
					{
						histo3d[i][j][k]=0;
					}
				}
		}

		
		for (int i = 0; i < Height*Width; i++)
			{
				unsigned char r = inRGBData[3*i+2];
				unsigned char g = inRGBData[3*i+1];
				unsigned char b = inRGBData[3*i];
				//fprintf(fd,"%d,%d,%d \n", r,g,b);
				histo3d[b][g][r]++;
			}


		for(int i=0;i<256;i++)
		{
			for(int j=0;j<256;j++)
			{
				for(int k=0;k<256;k++)
				{
					if(histo3d[i][j][k]>100)
					{
				
							fprintf(fd,"[%d %d %d ]=", i,j,k);
							fprintf(fd,"%d \n",histo3d[i][j][k]);
					
					}	
				}
			}
		}
		
	
	fclose(fd);
}







void MyImage::computebitssplitforRGB()
{
	FILE *fd = fopen("bitsplit.txt","w");

	fprintf(fd,"number of cells = %d",n);
	fprintf(fd,"type of algo = %d",m);
	rbits=gbits=bbits=0;

	double logofn = (double)log10((double)n)/(double)log10(2.0);

	logofn = ceil(logofn);
	fprintf(fd,"\nlogn=%f",logofn);
	

	double temp = logofn;
    for(int i =0;i<=temp;i++)
	{
		if(i==temp) break; rbits++;i++; 
		if(i==temp) break; gbits++; i++;
		if(i==temp) break; bbits++;

	}

		rlevel = pow(2,rbits);
		glevel = pow(2,gbits);
		blevel = pow(2,bbits);


		temp = 256.0/(double)rlevel;
		rstep = (temp);     // this will create n values for n-1 levels

		temp = 256.0/(double)glevel;
		gstep = (temp);     // this will create n values for n-1 levels
		
		temp = 256.0/(double)blevel;
		bstep = (temp);     // this will create n values for n-1 levels


		fprintf(fd,"\nrbits= %f gbits=%f  bbits=%f",rbits,gbits,bbits);
		fprintf(fd,"\nrlevel= %f glevel=%f  blevel=%f",rlevel,glevel,blevel);
		fprintf(fd,"\nrstep= %f gstep=%f  bstep=%f",rstep,gstep,bstep);
		
		fclose(fd);
}








void MyImage::createCodeBook()
{
	FILE * fd = fopen("createCodeBook.txt","w");
	

		for (int i = 0; i < Height*Width; i++)
		{
			
			
			int q1value =((double)inRGBData[3*i]/bstep);
			int q2value = ((double)inRGBData[3*i+1]/gstep);
			int q3value = ((double)inRGBData[3*i+2]/rstep);

				s_codebook[(int)q1value][(int)q2value][(int)q3value].blue  += ((double)inRGBData[3*i]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].green += ((double)inRGBData[3*i+1]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].red   += ((double)inRGBData[3*i+2]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].count++;
			
			fprintf(fd,"%d %d %d \n",q1value,q2value,q3value);		
		}
//find average
		for(int i=0;i<256;i++)
		{
			for(int j=0;j<256;j++)
			{
				for(int k=0;k<256;k++)
				{
					int count = 0;
					count = s_codebook[i][j][k].count ;
					if(count!=0)
					{
							s_codebook[i][j][k].blue /= (double)count;
							s_codebook[i][j][k].green /= (double)count;
							s_codebook[i][j][k].red /= (double)count;
							fprintf(fd,"blue=%f green=%f red=%f \n",s_codebook[i][j][k].blue,s_codebook[i][j][k].green,s_codebook[i][j][k].red);
					}
				}
			}
		}

	fclose(fd);

}

void MyImage::encodeImage()
{
	FILE *fd = fopen("ENCODEDIMAGE.txt","w");
	encodedImage = new unsigned char[Width*Height*3];
		for (int i = 0; i < Height*Width; i++)
		{
			int q1value = ((double)inRGBData[3*i]/bstep);
			int q2value = ((double)inRGBData[3*i+1]/gstep);
			int q3value = ((double)inRGBData[3*i+2]/rstep);
			encodedImage[3*i] = q1value;
			encodedImage[3*i+1] = q2value;
			encodedImage[3*i+2] = q3value;
			fprintf(fd,"%d %d %d \n",q1value,q2value,q3value);		
		}
	fclose(fd);
}


void MyImage::decodeImage()
{


	FILE * fd = fopen("DecodedImage.txt","w");
	FILE * fd2 = fopen("InputImage.txt","w");	

		for (int i = 0; i < Height*Width; i++)
		{
			int r,g,b;
			b = encodedImage[3*i];
			g = encodedImage[3*i+1];
			r = encodedImage[3*i+2];
			outRGBData[3*i] = s_codebook[b][g][r].blue;
			outRGBData[3*i+1] = s_codebook[b][g][r].green;
			outRGBData[3*i+2] = s_codebook[b][g][r].red;
			fprintf(fd,"%d %d %d \n",outRGBData[3*i],outRGBData[3*i+1],outRGBData[3*i+2]);		
			fprintf(fd2,"%d %d %d \n",inRGBData[3*i],inRGBData[3*i+1],inRGBData[3*i+2]);		
		}

fclose(fd);
fclose(fd2);
}


void MyImage::createchannelizedImage()
{
	FILE *fd = fopen("Rgrayscale.txt","w");
	rImage= new unsigned char[Width*Height*3];
	gImage= new unsigned char[Width*Height*3];
	bImage= new unsigned char[Width*Height*3];

	for(int i =0;i<Height*Width;i++)
	{
		rImage[3*i+2] = (int)abs((int)outRGBData[3*i+2]-(int)inRGBData[3*i+2]);
		rImage[3*i+1] = (int)abs((int)outRGBData[3*i+2]-(int)inRGBData[3*i+2]);
		rImage[3*i] =   (int)abs((int)outRGBData[3*i+2]-(int)inRGBData[3*i+2]);

		gImage[3*i+2] = (int)abs((int)outRGBData[3*i+1]-(int)inRGBData[3*i+1]);
		gImage[3*i+1] = (int)abs((int)outRGBData[3*i+1]-(int)inRGBData[3*i+1]);
		gImage[3*i] =   (int)abs((int)outRGBData[3*i+1]-(int)inRGBData[3*i+1]);

		bImage[3*i+2] = (int)abs((int)outRGBData[3*i]-(int)inRGBData[3*i]);
		bImage[3*i+1] = (int)abs((int)outRGBData[3*i]-(int)inRGBData[3*i]);
		bImage[3*i] =   (int)abs((int)outRGBData[3*i]-(int)inRGBData[3*i]);

	}

	fclose(fd);
}





/***************************************************************************************/


void MyImage::computesmartbitssplitforRGB()
{

	rbits=gbits=bbits=0;
	double logofn = (double)log10((double)n)/(double)log10(2.0);
	logofn = ceil(logofn);
	double temp = logofn;
    for(int i =0;i<=temp;i++)
	{
		if(i==temp) break; rbits++;i++; 
		if(i==temp) break; gbits++; i++;
		if(i==temp) break; bbits++;

	}

		rlevel = pow(2,rbits);
		glevel = pow(2,gbits);
		blevel = pow(2,bbits);


		temp = 256.0/(double)rlevel;
		rstep = (temp);     // this will create n values for n-1 levels

		temp = 256.0/(double)glevel;
		gstep = (temp);     // this will create n values for n-1 levels
		
		temp = 256.0/(double)blevel;
		bstep = (temp);     // this will create n values for n-1 levels

}

void MyImage::createsmartCodeBook()
{
	FILE * fd = fopen("createCodeBook.txt","w");
	

		for (int i = 0; i < Height*Width; i++)
		{

			double tempbstep = bstep;
			double tempgstep = gstep;
			double temprstep = rstep;
			
			unsigned char r = inRGBData[3*i+2];
			unsigned char g = inRGBData[3*i+1];
			unsigned char b = inRGBData[3*i];
				//fprintf(fd,"%d,%d,%d \n", r,g,b);
			if(histo3d[b][g][r]>=THRESHOLD)
			{
				tempbstep = tempbstep-((double)histo3d[b][g][r]/tempbstep);
				tempgstep = tempgstep-((double)histo3d[b][g][r]/tempgstep);
				temprstep = temprstep-((double)histo3d[b][g][r]/temprstep);
			}
			else if(histo3d[b][g][r]<THRESHOLD)
			{
				tempbstep = tempbstep+((double)histo3d[b][g][r]/tempbstep);
				tempgstep = tempgstep+((double)histo3d[b][g][r]/tempgstep);
				temprstep = temprstep+((double)histo3d[b][g][r]/temprstep);
			}
			



				int q1value =((double)inRGBData[3*i]/tempbstep);
				int q2value = ((double)inRGBData[3*i+1]/tempgstep);
				int q3value = ((double)inRGBData[3*i+2]/temprstep);

				s_codebook[(int)q1value][(int)q2value][(int)q3value].blue  += ((double)inRGBData[3*i]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].green += ((double)inRGBData[3*i+1]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].red   += ((double)inRGBData[3*i+2]);
				s_codebook[(int)q1value][(int)q2value][(int)q3value].count++;
			
			fprintf(fd,"%d %d %d \n",q1value,q2value,q3value);		
		}
		//find average
		for(int i=0;i<256;i++)
		{
			for(int j=0;j<256;j++)
			{
				for(int k=0;k<256;k++)
				{
					int count = 0;
					count = s_codebook[i][j][k].count ;
					if(count!=0)
					{
							s_codebook[i][j][k].blue /= (double)count;
							s_codebook[i][j][k].green /= (double)count;
							s_codebook[i][j][k].red /= (double)count;
							fprintf(fd,"blue=%f green=%f red=%f \n",s_codebook[i][j][k].blue,s_codebook[i][j][k].green,s_codebook[i][j][k].red);
					}
				}
			}
		}

	fclose(fd);
}




void MyImage::smartencodeImage()
{

	FILE *fd = fopen("ENCODEDIMAGE.txt","w");
	encodedImage = new unsigned char[Width*Height*3];
		for (int i = 0; i < Height*Width; i++)
		{

			double tempbstep = bstep;
			double tempgstep = gstep;
			double temprstep = rstep;

			unsigned char r = inRGBData[3*i+2];
			unsigned char g = inRGBData[3*i+1];
			unsigned char b = inRGBData[3*i];
			//fprintf(fd,"%d,%d,%d \n", r,g,b);

			if(histo3d[b][g][r]>=THRESHOLD)
			{
				tempbstep = tempbstep-((double)histo3d[b][g][r]/tempbstep);
				tempgstep = tempgstep-((double)histo3d[b][g][r]/tempgstep);
				temprstep = temprstep-((double)histo3d[b][g][r]/temprstep);
			}
			else if(histo3d[b][g][r]<THRESHOLD)
			{
				tempbstep = tempbstep+((double)histo3d[b][g][r]/tempbstep);
				tempgstep = tempgstep+((double)histo3d[b][g][r]/tempgstep);
				temprstep = temprstep+((double)histo3d[b][g][r]/temprstep);
			}
			
			int q1value = ((double)inRGBData[3*i]/tempbstep);
			int q2value = ((double)inRGBData[3*i+1]/tempgstep);
			int q3value = ((double)inRGBData[3*i+2]/temprstep);
			encodedImage[3*i] = q1value;
			encodedImage[3*i+1] = q2value;
			encodedImage[3*i+2] = q3value;
			fprintf(fd,"%d %d %d \n",q1value,q2value,q3value);		
		}
	fclose(fd);

}
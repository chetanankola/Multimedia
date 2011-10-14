// Image.cpp : Defines the entry point for the application.
//

#include "Image.h"
#include <cmath>



MyImage myImage;
int printfile=0;  //global variables 
int clamping=1;   //clamps the YUV to RGB converted values after rounding them.
double THRESHOLD = 10;
#define MAX_LOADSTRING 100
#define IMGHEIGHT 288
#define IMGWIDTH 352
#define MAX_SUBSAMPLINGVALUE 8
#define round(a) ((a>0)?((int)(a+0.5)):((int)(a-0.5)))
#define ceil(a) ((int)(a-0.5))
#define floor(a) ((int)(a+0.5))


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

	int y,u,v,q;   //Subsampling variables and quantization
	
	char ImagePath[_MAX_PATH];
	//sscanf(lpCmdLine, "%s %d %d", &ImagePath, &w, &h);
	sscanf(lpCmdLine, "%s %d %d %d %d %d", &ImagePath, &y,&u,&v,&q, &printfile,&clamping,&THRESHOLD);
	myImage.setWidth(IMGWIDTH);
	myImage.setHeight(IMGHEIGHT);
	myImage.setYUVQ(y,u,v,q);
	myImage.setImagePath(ImagePath);
	myImage.ReadImage();
	myImage.ConvertRGBtoYUV();
	myImage.SubsampleYUV();
	myImage.UpsampleYUV();
	myImage.ConvertYUVtoRGB();
	myImage.Quantize();




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

	for (i = 0; i < Height*Width; i++)
	{
		Data[3*i]	= Bbuf[i];
		Data[3*i+1]	= Gbuf[i];
		Data[3*i+2]	= Rbuf[i];
	}

	delete [] Rbuf;
	delete [] Gbuf;
	delete [] Bbuf;

	fclose(IN_FILE);
	
}



/*Y = | 0.299 0.587 0.114  | R
  U = |-0.147 -0.289 0.436 | G
  V = |0.615 -0.515 -0.1   | B

  Y =  0.114*B +  0.587*G  +  0.299 *R
  U =  0.436*B + -0.289*G  + -0.147*R
  V = -0.1*B   + -0.515*G  +  0.615*R
 */

void MyImage::ConvertRGBtoYUV()
{
	if(printfile)
	{
		FILE *fd2 = fopen("originalRGB.txt","w");
		for (int i = 0; i < Height*Width; i++)
		{
				fprintf(fd2,"%d %d %d     ",Data[3*i],Data[3*i+1],Data[3*i+2]);
				if(i%Width==0)
				fprintf(fd2,"\n");
		}
		fclose(fd2);
	}
	YUVData = new double[Width*Height*3];
	outRGBData = new unsigned char[Width*Height*3];
	YUVcharData = new char[Width*Height*3];
	for (int i = 0; i < Height*Width; i++)
	{
		/*YUV AS DOUBLE DATA*/
		YUVData[3*i]	= (0.114*Data[3*i])+(0.587*Data[3*i+1])+ (0.299 *Data[3*i+2]);
		YUVData[3*i+1]	= (0.436*Data[3*i])+(-0.289*Data[3*i+1])+ (-0.147 *Data[3*i+2]);
		YUVData[3*i+2]	= (-0.1*Data[3*i])+(-0.515*Data[3*i+1])+ (0.615 *Data[3*i+2]);


		/*
		Doesnt make any sense : 
		Rounding is to be done only when you want to move from double to char and that from YUV TO RGB only.
		because YUV values don range from -128 to 127 :
		copy of YUV IN A CHAR DATA 
		YUVcharData[3*i]   = round(YUVData[3*i]);
		YUVcharData[3*i+1] = round(YUVData[3*i+1]);
		YUVcharData[3*i+2] = round(YUVData[3*i+2]);
		*/
	}



		if(printfile)
		{
			FILE *fd3 = fopen("YUVDouble.txt","w");
			for (int i = 0; i < Height*Width; i++)
			{
					
					if(i%Width==0)
					fprintf(fd3,"\n");

					fprintf(fd3,"%f %f %f     ",YUVData[3*i],YUVData[3*i+1],YUVData[3*i+2]);
			}
			fclose(fd3);
		}

}




//Sender always subsamples/removes some comp before sending
void MyImage::SubsampleYUV()		 
{
	//YUVsubsampledData
	YUVsubsampledData = new double[Width*Height*3];


	int ysubcount=0;
	int usubcount=0;
	int vsubcount=0;

	//To denote compression I will store a value of Zero in the places where the sample is removed
	//and assign proper values to these empty places during upsampling(without the assumption that zero denotes an empty samply)

    // eg: 4 1 1 after 0th sample skip 4 samples and then take 
	for (int i = 0; i < Height*Width; i++)
	{
		/*YUV AS DOUBLE DATA*/
		if(i==ysubcount){ YUVsubsampledData[3*i]	= YUVData[3*i];   ysubcount+=Y;}   else{YUVsubsampledData[3*i]		= 0; }
		if(i==usubcount){ YUVsubsampledData[3*i+1]	= YUVData[3*i+1]; usubcount+=U;}   else{YUVsubsampledData[3*i+1]    = 0; }
		if(i==vsubcount){ YUVsubsampledData[3*i+2]	= YUVData[3*i+2]; vsubcount+=V;}   else{YUVsubsampledData[3*i+2]	= 0; }	
	}

	for (int i = 0; i < Height*Width; i++)
	{
		YUVData[3*i]   = YUVsubsampledData[3*i];
		YUVData[3*i+1] = YUVsubsampledData[3*i+1];
		YUVData[3*i+2] = YUVsubsampledData[3*i+2];

	}
	
		if(printfile)
		{	
			FILE *fd5 = fopen("SUBsampledYUVDoublematrix.txt","w");
			for (int i = 0; i < Height*Width; i++)
			{
					if(i%Width==0)
					fprintf(fd5,"\n");

					fprintf(fd5,"%f %f %f     ",YUVsubsampledData[3*i],YUVsubsampledData[3*i+1],YUVsubsampledData[3*i+2]);

			}
			fclose(fd5);
		}


}


//receiver always upsamples it and fills in missing values
void MyImage::UpsampleYUV()
{
	YUVupsampledData = new double[Width*Height*3];
    //YUVupsampledData
	int ysubcount=0;
	int usubcount=0;
	int vsubcount=0;

	//To denote compression I will store a value of Zero in the places where the sample is removed
    // eg: 4 1 1 after 0th sample skip 4 samples and then take 
	for (int i = 0; i < Height*Width; i++)
	{
		/*YUV AS DOUBLE DATA*/
		double left;
		double right;
		double store;
		double test;
		if(i==ysubcount)//we have a proper value so the next proper value should be at i+Y
		{
			left  = YUVData[3*i];   
			right = YUVData[3*(i+Y)];
			
			test=left-right;
			if(test<0)test=test*(-1);
			
			if(test>THRESHOLD)
			{
				store=left;
			}
			else
			{
				store = (left+right)/2.0;
			}

			if(left==right)
			{
				store=left;
			}
				for(int j=1;j<Y;j++)
				YUVData[3*(i+j)] = store;
			
			ysubcount+=Y;
		}   


		if(i==usubcount)
		{ 
			left  = YUVData[3*i+1];   
			right = YUVData[3*(i+U)+1];
			test=left-right;
			if(test<0)test=test*(-1);
			
			if(test>THRESHOLD)
			{
				store=left;
			}
			else
			{
				store = (left+right)/2.0;
			}
			
			if(left==right)
			{
				store=left;
			}

			for(int j=1;j<U;j++)
			YUVData[3*(i+j)+1]	= store; 
			usubcount+=U;
		}


		if(i==vsubcount)
		{ 
			left  = YUVData[3*i+2];   
			right = YUVData[3*(i+V)+2];
			test=left-right;
			if(test<0)test=test*(-1);
			
			if(test>THRESHOLD)
			{
				store=left;
			}
			else
			{
				store = (left+right)/2.0;
			}

			if(left==right)
			{
				store=left;
			}
			for(int j=1;j<V;j++)
			YUVData[3*(i+j)+2]	= store; 
			vsubcount+=V;
		}   
	}//end of for loop



	for (int i = 0; i < Height*Width; i++)
	{
		YUVupsampledData[3*i]   = YUVData[3*i];
		YUVupsampledData[3*i+1] = YUVData[3*i+1];
		YUVupsampledData[3*i+2] = YUVData[3*i+2];
	}
	
		if(printfile)
		{	
			FILE *fd5 = fopen("UPsampledYUVDoublematrix.txt","w");
			for (int i = 0; i < Height*Width; i++)
			{
					if(i%Width==0)
					fprintf(fd5,"\n");

					fprintf(fd5,"%f %f %f     ",YUVupsampledData[3*i],YUVupsampledData[3*i+1],YUVupsampledData[3*i+2]);

			}
			fclose(fd5);
		}



	
}




/* YUV TO OUTPUT RGB CONVERSION *****************************
  R 0.999 0.000 1.140 Y
  G = 1.000 -0.395 -0.581 U
  B 1.000 2.032 -0.000 V


  R =  0.999*Y +  0.000*U  +  1.140 *V
  G =  1.000*Y + -0.395*U  + -0.581*V
  B =  1.000*Y   + 2.032*U  +  -0.000*V
*/
void MyImage::ConvertYUVtoRGB()
{
	double *temp = new double[Height*Width*3];
	for (int i = 0; i < Height*Width; i++)
	{
		/*temp[] as a DOUBLE DATA */
		/*B VALUE */temp[3*i]	= round( (1.000*YUVData[3*i])+(2.032*YUVData[3*i+1])+ (-0.000 *YUVData[3*i+2]) );
		/*G VALUE */temp[3*i+1]	= round( (1.000*YUVData[3*i])+(-0.395*YUVData[3*i+1])+ (-0.581 *YUVData[3*i+2]));
		/*R VALUE */temp[3*i+2]	= round( (0.999*YUVData[3*i])+(0.000*YUVData[3*i+1])+ (1.140 *YUVData[3*i+2]));

		if(clamping)
		{
				if(temp[3*i]>127)temp[3*i]=127;		if(temp[3*i]<-128)temp[3*i]=-128;
				if(temp[3*i+1]>127)temp[3*i+1]=127;	if(temp[3*i+1]<-128)temp[3*i+1]=-128;
				if(temp[3*i+2]>127)temp[3*i+2]=127;	if(temp[3*i+2]<-128)temp[3*i+2]=-128;
		}		
		/*RGB AS CHAR DATA : Values after clamping --NOTE: OutRGBData is UNSIGNEDCHAR*/
		outRGBData[3*i]		= temp[3*i];
		outRGBData[3*i+1]	= temp[3*i+1];
		outRGBData[3*i+2]	= temp[3*i+2];



		/*Values of output without clamping */
		/*outRGBData[3*i]	= (1.000*YUVData[3*i])+(2.032*YUVData[3*i+1])+ (-0.000 *YUVData[3*i+2]);
		outRGBData[3*i+1]	= (1.000*YUVData[3*i])+(-0.395*YUVData[3*i+1])+ (-0.581 *YUVData[3*i+2]);
		outRGBData[3*i+2]	= (0.999*YUVData[3*i])+(0.000*YUVData[3*i+1])+ (1.140 *YUVData[3*i+2]); */
	}//end of for loop



		if(printfile)
		{	
			FILE *fd1 = fopen("OutputRGBDoublematrix.txt","w");
			for (int i = 0; i < Height*Width; i++)
			{
					
					if(i%Width==0)
					fprintf(fd1,"\n");

				fprintf(fd1,"%f %f %f     ",temp[3*i],temp[3*i+1],temp[3*i+2]);
			}
			fclose(fd1);
		}
	delete temp;
}






void MyImage::Quantize()
{
double stepsize=1;
double temp=1;
	if(Q==0 ||Q>256 || Q<0 ) return;
	
	if(Q==1)
	{
		
		//quantization code goes here:
		for (int i = 0; i < Height*Width; i++)
		{
			outRGBData[3*i] =0;
			outRGBData[3*i+1] =0;
			outRGBData[3*i+2] =0;
		}


	}
	else
	{
		Q--;
		//quantization code goes here:
		temp = 256.0/(double)Q;
		stepsize = round(temp);     // this will create n values for n-1 levels
	

/*
if Q = 2
then stepsize = 128

lets take a sample:
0 45 240   130

0 --> 0/128 = 0 *128 = 0
45---> 45/128 = 0 = 0*128 = 0  
so all values from 
0 to 64 = 0
65 to 191 = 1 = 1*128 = 128
192 to 255 = 2  = 2*128 = 256
*/
		for (int i = 0; i < Height*Width; i++)
		{
			double q1value = round((double)outRGBData[3*i]/stepsize);
			q1value = q1value *stepsize;
			if(q1value>255)q1value=255;
			outRGBData[3*i] =q1value;

			double q2value = round((double)outRGBData[3*i+1]/stepsize);
			q2value = q2value *stepsize;
			if(q2value>255)q2value=255;
			outRGBData[3*i+1] =q2value;

			double q3value = round((double)outRGBData[3*i+2]/stepsize);
			q3value = q3value *stepsize;
			if(q3value>255)q3value=255;
			outRGBData[3*i+2] =q3value;

		}

	}//else if Q is not 1




	if(printfile)
	{
		FILE *fd2 = fopen("outputRGBUnsignedCharmatrix.txt","w");
		fprintf(fd2,"*************\n\nstepsize=%f  Q= %d  \n\n***************\n",stepsize,Q);
		for (int i = 0; i < Height*Width; i++)
		{
				
				if(i%Width==0)
				fprintf(fd2,"\n");

				fprintf(fd2,"%d %d %d     ",outRGBData[3*i],outRGBData[3*i+1],outRGBData[3*i+2]);
		}
		fclose(fd2);


		FILE *fd4 = fopen("YUVCharmatrix.txt","w");
		for (int i = 0; i < Height*Width; i++)
		{
				
				if(i%Width==0)
				fprintf(fd4,"\n");

				fprintf(fd4,"%d %d %d     ",YUVcharData[3*i],YUVcharData[3*i+1],YUVcharData[3*i+2]);
		}
		fclose(fd4);

	}//End of If(printfile)


} //end of quantize function

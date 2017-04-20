/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.yzi.doutu.operate;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.yzi.doutu.utils.CommUtil;

/**
 * @author jarlen
 */
public class TextObject extends ImageObject
{

	private int textSize = 80;
	private int color = Color.BLACK;
	private Typeface typeface;
	private String text;
	private boolean bold = false;
	private boolean italic = false;
	private Context context;

	Paint paint = new Paint();

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param text
	 *            输入的文字
	 * @param x
	 *            位置x坐标
	 * @param y
	 *            位置y坐标
	 * @param rotateBm
	 *            旋转按钮的图片
	 * @param deleteBm
	 *            删除按钮的图片
	 */
	public TextObject(Context context, String text, int x, int y,
			Bitmap rotateBm, Bitmap deleteBm)
	{
		super(text);
		this.context = context;
		this.text = text;
		mPoint.x = x;
		mPoint.y = y;
		this.rotateBm = rotateBm;
		this.deleteBm = deleteBm;
		regenerateBitmap();
	}

	public TextObject()
	{
	}

	/**
	 * 绘画出字体
	 */
	public void regenerateBitmap()
	{
		textSize=CommUtil.sp2px(30);
		paint.setAntiAlias(true);
		paint.setTextSize(textSize);
		paint.setTypeface(typeface);
		paint.setColor(color);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setDither(true);
		paint.setStrokeWidth(3); //设置描边宽度
		paint.setFlags(Paint.SUBPIXEL_TEXT_FLAG);
		String lines[] = text.split("\n");

		int textWidth = 0;
		for (String str : lines)
		{
			int temp = (int) paint.measureText(str);
			if (temp > textWidth)
				textWidth = temp;
		}
		if (textWidth < 1)
			textWidth = 1;
		if (srcBm != null)
			srcBm.recycle();
		srcBm = Bitmap.createBitmap(textWidth, textSize * (lines.length) + 8,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(srcBm);
		canvas.drawARGB(0, 0, 0, 0);
		for (int i = 1; i <= lines.length; i++)
		{
			canvas.drawText(lines[i - 1], 0, i * textSize, paint);
		}
		setCenter();
	}



	/**
	 * 设置属性值后，提交方法
	 */
	public void commit()
	{
		regenerateBitmap();
	}

	/**
	 * 公共的getter和setter方法
	 */
	public int getTextSize()
	{
		return textSize;
	}

	public void setTextSize(int textSize)
	{
		this.textSize = textSize;
	}

	public int getColor()
	{
		return color;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public Typeface getTypeface()
	{
		return typeface;
	}

	public void setTypeface(Typeface typeface)
	{
		this.typeface = typeface;
	}

	public boolean isBold()
	{
		return bold;
	}

	public void setBold(boolean bold)
	{
		this.bold = bold;
	}

	public boolean isItalic()
	{
		return italic;
	}

	public void setItalic(boolean italic)
	{
		this.italic = italic;
	}

	public int getX()
	{
		return mPoint.x;
	}

	public void setX(int x)
	{
		this.mPoint.x = x;
	}

	public int getY()
	{
		return mPoint.y;
	}

	public void setY(int y)
	{
		this.mPoint.y = y;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

}

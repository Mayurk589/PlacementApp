package com.proj.placementapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PdfPagerAdapter extends PagerAdapter {

    private Context mContext;
    private PdfRenderer mPdfRenderer;
    private int mPageCount;
    private Map<Integer, Bitmap> mBitmapCache = new HashMap<>();

    public PdfPagerAdapter(Context context, ParcelFileDescriptor fileDescriptor) {
        mContext = context;
        try {
            mPdfRenderer = new PdfRenderer(fileDescriptor);
            mPageCount = mPdfRenderer.getPageCount();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_pdf_page, container, false);
        PhotoView photoView = view.findViewById(R.id.photoView);

        Bitmap bitmap = mBitmapCache.get(position);
        if (bitmap == null) {
            try {
                bitmap = renderPage(position);
                mBitmapCache.put(position, bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            photoView.setImageBitmap(bitmap);
            container.addView(view);
        }

        return view;
    }

    private Bitmap renderPage(int position) throws IOException {
        if (mPdfRenderer == null || mPageCount <= position) {
            return null;
        }

        try {
            PdfRenderer.Page page = mPdfRenderer.openPage(position);
            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
            return bitmap;
        } catch (IllegalStateException e) {
            // Handle the exception here, for example, by logging the error
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        // Override this method to force the ViewPager to update the view when notifyDataSetChanged() is called
        return POSITION_NONE;
    }
}

package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.estasvegano.android.estasvegano.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class CodeReaderFragment extends Fragment implements ZBarScannerView.ResultHandler
{
    @Bind(R.id.reader_scanner)
    ZBarScannerView scannerView;
    private OnCodeReadedListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_code_readed, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult)
    {
        if (listener != null)
        {
            listener.onCodeReaded(rawResult.getContents(), rawResult.getBarcodeFormat());
        }
    }

    public void setListener(OnCodeReadedListener listener)
    {
        this.listener = listener;
    }

    public interface OnCodeReadedListener
    {
        void onCodeReaded(String result, BarcodeFormat format);
    }
}

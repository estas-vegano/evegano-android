package com.estasvegano.android.estasvegano.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.estasvegano.android.estasvegano.R;
import com.estasvegano.android.estasvegano.entity.Product;
import com.estasvegano.android.estasvegano.view.util.PicassoCircleBorderTransform;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.estasvegano.android.estasvegano.view.util.Utils.getEthicalImage;
import static com.estasvegano.android.estasvegano.view.util.Utils.getTypeImage;

@FragmentWithArgs
public class ViewProductFragment extends BaseFragment {

    @Inject
    Picasso picasso;

    @Bind(R.id.f_view_product_toolbar)
    Toolbar toolbar;

    @Bind(R.id.f_view_product_image)
    ImageView productImageView;

    @Bind(R.id.f_view_product_type_image)
    ImageView productTypeImageView;

    @Bind(R.id.f_view_product_ethical_image)
    ImageView productEthicalImageView;

    @SuppressWarnings("NullableProblems") // @Arg
    @Arg
    @NonNull
    Product product;

    @Nullable
    private ViewProductFragmentListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getVeganoApplication().getComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_view_product, container, false);
        ButterKnife.bind(this, v);
        FragmentArgs.inject(this);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        //noinspection ConstantConditions can not be null here
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindProduct();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void bindProduct() {
        setTitle(product.title());
        loadAvatar();
        productTypeImageView.setImageResource(getTypeImage(product.info()));
        productEthicalImageView.setImageResource(getEthicalImage(product.producer().ethical()));
    }

    private void loadAvatar() {
        int borderColor = ContextCompat.getColor(getActivity(), R.color.accent);
        int borderWidth = getResources().getDimensionPixelSize(R.dimen.border_width);
        picasso.load(product.photo())
                .placeholder(R.drawable.product_placeholder)
                .transform(new PicassoCircleBorderTransform(borderColor, borderWidth))
                .into(productImageView);
    }

    @OnClick(R.id.f_view_product_complain_button)
    void onComplainClicked() {
        if (listener != null) {
            listener.onComplainClicked(product);
        }
    }

    public void setListener(@Nullable ViewProductFragmentListener listener) {
        this.listener = listener;
    }

    public interface ViewProductFragmentListener {
        void onComplainClicked(@NonNull Product product);
    }
}

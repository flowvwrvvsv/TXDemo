package com.tianxie.demo.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tu.circlelibrary.CirclePercentBar;
import com.aries.library.fast.manager.GlideManager;
import com.tianx.demo.R;
import com.tianxie.demo.base.BaseItemTouchQuickAdapter;
import com.tianxie.demo.base.BaseItemTouchViewHolder;
import com.tianxie.demo.entity.FaceResultWidgetEntity;

import java.util.ArrayList;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

public class FaceResultWidgetAdapter extends BaseItemTouchQuickAdapter<FaceResultWidgetEntity, BaseItemTouchViewHolder>
{
    int width = -1;

    public FaceResultWidgetAdapter()
    {
        super(R.layout.layout_face_result_item, new ArrayList<>());
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, FaceResultWidgetEntity item)
    {
        if (width == -1)
        {
            RecyclerView recyclerView = getRecyclerView();
            width = recyclerView.getWidth() / 2;
        }

        ViewGroup.LayoutParams layoutParams = helper.getView(R.id.face_container_iamge).getLayoutParams();
        layoutParams.height = width - 20;

        if (item != null)
        {
            if (item.getName() != null)
            {
                helper.setText(R.id.item_name, item.getName())
                        .setText(R.id.item_sex, item.getSex())
                        .setText(R.id.item_document_id, item.getDocumentID());
            }
            if (item.getFaceUserEntitiy().getScore() >= 50)
            {
                helper.setText(R.id.face_result_item_similarity_text, "相似度");
                ((TextView) helper.getView(R.id.face_result_item_similarity_text)).setTextColor(ActivityCompat.getColor(getRecyclerView().getContext(), R.color.white));
                helper.getView(R.id.item_all).setBackgroundResource(R.drawable.shape_search_find_item_all_bkg);
                helper.getView(R.id.item_container_native).setBackgroundResource(R.drawable.shape_search_find_item);
                helper.getView(R.id.item_container_net).setBackgroundResource(R.drawable.shape_search_find_item);
                helper.getView(R.id.item_container_text).setBackgroundResource(R.color.login_btn);
                helper.getView(R.id.item_container_info).setVisibility(View.VISIBLE);
                helper.getView(R.id.item_sanfei_text).setVisibility(View.GONE);
                ((TextView) helper.getView(R.id.item_more_info)).setText("更多信息");

            } else
            {
                helper.setText(R.id.face_result_item_similarity_text, "没记录");
                ((TextView) helper.getView(R.id.face_result_item_similarity_text)).setTextColor(ActivityCompat.getColor(getRecyclerView().getContext(), R.color.color_face_result_red));
                helper.getView(R.id.item_all).setBackgroundResource(R.drawable.shape_face_result_red);
                helper.getView(R.id.item_container_native).setBackgroundResource(R.drawable.shape_face_result_image_red);
                helper.getView(R.id.item_container_net).setBackgroundResource(R.drawable.shape_face_result_image_red);
                helper.getView(R.id.item_container_text).setBackgroundResource(R.color.color_face_result_red);
                helper.getView(R.id.item_container_info).setVisibility(View.GONE);
                helper.getView(R.id.item_sanfei_text).setVisibility(View.VISIBLE);
                ((TextView) helper.getView(R.id.item_more_info)).setText("三非登记");
            }

            float score = item.getFaceUserEntitiy().getScore();
            CirclePercentBar circlePercentBar = helper.getView(R.id.circle_bar);
            circlePercentBar.setPercentData(score, "%", new DecelerateInterpolator());

            GlideManager.loadImg(item.getNativeUrl(), helper.getView(R.id.face_result_native), R.drawable.shape_scan_base_error_image);
            if (score >= 50)
            {
                GlideManager.loadImg(item.getFaceUserEntitiy().getNetUrl(), helper.getView(R.id.face_result_net), R.drawable.shape_scan_base_error_image);
            } else
                ((ImageView) helper.getView(R.id.face_result_net)).setImageDrawable(ActivityCompat.getDrawable(getRecyclerView().getContext(), R.drawable.shape_scan_base_error_image));

            helper.addOnClickListener(R.id.item_all);
        }
    }
}

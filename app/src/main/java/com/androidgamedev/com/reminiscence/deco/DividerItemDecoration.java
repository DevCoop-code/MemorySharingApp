package com.androidgamedev.com.reminiscence.deco;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration
{
    private final int dividerHeight;
    private Drawable divider;

    public DividerItemDecoration(Context context)
    {
        //기본인 ListView 구분선의 Drawable을 얻는다(구분선을 커스터마이징 하고 싶을 때는 여기서 Drawable을 가져온다)
        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        divider = a.getDrawable(0);

        //표시할 때 마다 높이를 가져오지 않아도 되도록 구해두기
        dividerHeight = divider.getIntrinsicHeight();
        a.recycle();
    }

    //View의 아이템보다 위에 그리고 싶을 때 사용하는 메서드
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDrawOver(c, parent, state);
    }

    //View의 아이템보다 아래에 그리고 싶을 때 사용하는 메서드
    //RecyclerView의 아이템마다 아래에 선을 그림
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
        super.onDraw(c, parent, state);

        //좌우의 padding으로 선의 right와 left를 설정
        final int lineLeft = parent.getPaddingLeft();
        final int lineRight = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for(int i=0; i<childCount; i++)
        {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();

            //애니메이션 등의 상황에서 제대로 이동하기 위함
            int childTransitionY = Math.round(ViewCompat.getTranslationY(child));
            final int top = child.getBottom() + params.bottomMargin + childTransitionY;
            final int bottom = top + dividerHeight;

            // View 아래에 선을 그린다
            divider.setBounds(lineLeft, top, lineRight, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // View 아래에 선이 들어가므로 아래에 Offset을 넣는다
        outRect.set(0, 0, 0, dividerHeight);
    }
}

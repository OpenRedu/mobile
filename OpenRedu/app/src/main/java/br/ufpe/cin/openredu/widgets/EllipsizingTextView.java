package br.ufpe.cin.openredu.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

public class EllipsizingTextView extends TextView {
    private static final String ELLIPSIS = "…";

    private TruncateAt mTruncateAt;
    private boolean isEllipsized;
    private boolean isStale;
    private boolean programmaticChange;
    private String fullText;
    private int maxLines = -1;
    private float lineSpacingMultiplier = 1.0f;
    private float lineAdditionalVerticalPadding = 0.0f;

    public EllipsizingTextView(Context context) {
        super(context);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isEllipsized() {
        return isEllipsized;
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        isStale = true;
    }

    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        this.lineAdditionalVerticalPadding = add;
        this.lineSpacingMultiplier = mult;
        super.setLineSpacing(add, mult);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        if (!programmaticChange) {
            fullText = text.toString();
            isStale = true;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (isStale) {
            super.setEllipsize(null);
            resetText();
        }
        super.onDraw(canvas);
    }

    private void resetText() {
        int maxLines = getMaxLines();
        String workingText = fullText;
        boolean ellipsized = false;
        if (maxLines != -1) {
            Layout layout = createWorkingLayout(workingText);
            if (layout.getLineCount() > maxLines) {
            	
                switch (mTruncateAt) {
				case START:
					workingText = fullText.substring(layout.getLineEnd(layout.getLineCount() - maxLines - 2)).trim();
	                while (createWorkingLayout(workingText + ELLIPSIS).getLineCount() > maxLines) {
	                    int firstSpace = workingText.indexOf(' ');
	                    if (firstSpace == -1) {
	                        break;
	                    }
	                    workingText = workingText.substring(firstSpace + 1);
	                }
					workingText = ELLIPSIS + workingText;
					break;
				case MIDDLE:
				case END:
				case MARQUEE:
					workingText = fullText.substring(0, layout.getLineEnd(maxLines - 1)).trim();
	                while (createWorkingLayout(workingText + ELLIPSIS).getLineCount() > maxLines) {
	                    int lastSpace = workingText.lastIndexOf(' ');
	                    if (lastSpace == -1) {
	                        break;
	                    }
	                    workingText = workingText.substring(0, lastSpace);
	                }
					workingText = workingText + ELLIPSIS;
					break;
				}
                ellipsized = true;
            }
        }
        if (!workingText.equals(getText())) {
            programmaticChange = true;
            try {
                setText(workingText);
            } finally {
                programmaticChange = false;
            }
        }
        isStale = false;
        isEllipsized = ellipsized;
    }

    private Layout createWorkingLayout(String workingText) {
        return new StaticLayout(workingText, getPaint(), getWidth() - getPaddingLeft() - getPaddingRight(),
                Alignment.ALIGN_NORMAL, lineSpacingMultiplier, lineAdditionalVerticalPadding, false);
    }

    @Override
    public void setEllipsize(TruncateAt where) {
    	mTruncateAt = where;
    }
}
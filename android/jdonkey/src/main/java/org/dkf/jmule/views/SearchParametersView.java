package org.dkf.jmule.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import org.dkf.jed2k.protocol.server.search.SearchRequest;
import org.dkf.jmule.R;

/**
 * Created by inkpot on 09.09.2016.
 */
public class SearchParametersView extends LinearLayout {

    private EditText editMinSize;
    private EditText editMaxSize;
    private EditText editSources;
    private EditText editCompleteSources;
    private RadioGroup types;
    private RadioGroup searchSources;

    public SearchParametersView(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.view_search_parameters, this);
        editMinSize = (EditText)findViewById(R.id.view_search_parameter_min_size);
        editMaxSize = (EditText)findViewById(R.id.view_search_parameter_max_size);
        editSources = (EditText)findViewById(R.id.view_search_parameter_sources);
        editCompleteSources = (EditText)findViewById(R.id.view_search_parameter_complete_sources);
        types = (RadioGroup)findViewById(R.id.view_search_parameter_file_type);
        searchSources = (RadioGroup)findViewById(R.id.view_search_source_type);
        editMinSize.setText("");
        editMaxSize.setText("");
        editSources.setText("");
        editCompleteSources.setText("");
    }

    public String getChecked() {
        int selectedId = types.getCheckedRadioButtonId();
        switch(selectedId) {
            case R.id.search_parameter_type_any: return "";
            case R.id.search_parameter_type_archive: return SearchRequest.ED2KFTSTR_ARCHIVE;
            case R.id.search_parameter_type_audio: return SearchRequest.ED2KFTSTR_AUDIO;
            case R.id.search_parameter_type_video: return SearchRequest.ED2KFTSTR_VIDEO;
            case R.id.search_parameter_type_cd_images: return SearchRequest.ED2KFTSTR_CDIMAGE;
            case R.id.search_parameter_picture: return SearchRequest.ED2KFTSTR_IMAGE;
            case R.id.search_parameter_type_document: return SearchRequest.ED2KFTSTR_DOCUMENT;
            case R.id.search_parameter_type_application:  return SearchRequest.ED2KFTSTR_PROGRAM;
        }

        return "";
    }

    public int getMinSize() {
        return (editMinSize.getText().toString().isEmpty())?0:Integer.parseInt(editMinSize.getText().toString());
    }

    public int getMaxSize() {
        return (editMaxSize.getText().toString().isEmpty())?0:Integer.parseInt(editMaxSize.getText().toString());
    }

    public int getSourcesCount() {
        return (editSources.getText().toString().isEmpty())?0:Integer.parseInt(editSources.getText().toString());
    }

    public int getCompleteSources() {
        return (editCompleteSources.getText().toString().isEmpty())?0:Integer.parseInt(editCompleteSources.getText().toString());
    }

    public boolean isSearchByServer() {
        return searchSources.getCheckedRadioButtonId() == R.id.search_source_type_server;
    }

    public void showSearchSourceChooser(boolean flag) {
        searchSources.setVisibility(flag?VISIBLE:GONE);
    }
}

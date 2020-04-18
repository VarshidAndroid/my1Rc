package com.rewardscards.a1rc.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rewardscards.a1rc.Model.MenuModel;
import com.rewardscards.a1rc.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listDataHeader;
    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listDataHeader,
                                 HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).menuName;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_child, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);


        ImageView img_menu = convertView
                .findViewById(R.id.img_menu);

        if (getChild(groupPosition, childPosition).code.equalsIgnoreCase("Report")){
            img_menu.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getChild(groupPosition, childPosition).code.equalsIgnoreCase("Contact")){
            img_menu.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getChild(groupPosition, childPosition).code.equalsIgnoreCase("Faq")){
            img_menu.setImageResource(R.drawable.ic_menu_gallery);
        }

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (this.listDataChild.get(this.listDataHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                    .size();
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = getGroup(groupPosition).menuName;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_header, null);
        }
      /*  ImageView img_menu_main = convertView.findViewById(R.id.img_menu_main);
        ImageView img_menu_main_drop = convertView.findViewById(R.id.img_menu_main_drop);
        if (getGroup(groupPosition).code.equalsIgnoreCase("Explore")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("Profile")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("Competition")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("Support")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
            img_menu_main_drop.setVisibility(View.VISIBLE);
            img_menu_main_drop.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("Setting")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("Terms")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }

        if (getGroup(groupPosition).code.equalsIgnoreCase("About")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }
        if (getGroup(groupPosition).code.equalsIgnoreCase("Logout")) {
            img_menu_main.setImageResource(R.drawable.ic_menu_gallery);
        }*/


        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

package com.lulixe.pulari.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lulixe.pulari.R;
import com.lulixe.pulari.model.Product;
import com.lulixe.pulari.model.ProductVarient;
import com.lulixe.pulari.model.QuantityConn;
import com.lulixe.pulari.utils.AppDb;
import com.lulixe.pulari.utils.FlipAnimation;
import com.lulixe.pulari.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuantityDialog extends Dialog {
    private Context c;
    private Product model;

    private TextView itemRemove;

    private int PRO_AUTO_ID = 0;

    private TextView title,unit;
    private Button addToList;
    private EditText qtyEd;
    private ImageView close;
    private LinearLayout qtyLayout;
    private Spinner varSpinner;
    private ImageView plusBt,minusBt;
    private TextView proVarQty;

    private AppDb db;
    private int INDEX = 0;

    public QuantityDialog(final Context context) {
        super(context);
        this.c = context;

        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_qty);
        if (this.getWindow()!=null){
            this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.getWindow().setDimAmount(0.3f);
        }
        setUI();

        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setCustomerQty(1);
                model.setPrice(model.getProVarList().
                        get(varSpinner.getSelectedItemPosition()).getPrice());
                model.setUnit(model.getProVarList().
                        get(varSpinner.getSelectedItemPosition()).getName());
                model.setVarID(model.getProVarList().
                        get(varSpinner.getSelectedItemPosition()).getId());
                db.insert(model);
                Toast.makeText(c,"Added to cart",Toast.LENGTH_SHORT).show();

                new FlipAnimation(context,
                        String.valueOf(new AppDb(context).getCartCount())).start();

                addToList.setVisibility(View.GONE);
                qtyLayout.setVisibility(View.VISIBLE);

                /*if(!qtyEd.getText().toString().isEmpty()){
                    if(PRO_AUTO_ID == 0){
                        model.setCustomerQty(1);
                        model.setPrice(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getPrice());
                        model.setUnit(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getName());
                        model.setVarID(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getId());
                        db.insert(model);
                        Toast.makeText(c,"Added to cart",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        model.setCustomerQty(1);
                        model.setPrice(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getPrice());
                        model.setUnit(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getName());
                        model.setVarID(model.getProVarList().
                                get(varSpinner.getSelectedItemPosition()).getId());
                        model.setDbID(PRO_AUTO_ID);
                        db.update(model);
                        Toast.makeText(c,"Added to cart",Toast.LENGTH_SHORT).show();
                    }

                    new FlipAnimation(context,
                            String.valueOf(new AppDb(context).getCartCount())).start();
                }*/

                QuantityConn conn = (QuantityConn) c;
                conn.refreshAdapter();

                if(INDEX != 0)
                    conn.refreshAdapter(INDEX,model);

                //dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        plusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(proVarQty.getText().toString());
                model.setCustomerQty(qty+1);
                model.setVarID(model.getProVarList().
                        get(varSpinner.getSelectedItemPosition()).getId());
                db.update(model);
                Toast.makeText(c,"Added to cart",Toast.LENGTH_SHORT).show();
                proVarQty.setText(String.valueOf(qty+1));

                new FlipAnimation(context,
                        String.valueOf(new AppDb(context).getCartCount())).start();
            }
        });

        minusBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(proVarQty.getText().toString());
                if(qty == 1){
                    //boolean flag = db.delSingle(PRO_AUTO_ID);
                    /*Toast.makeText(context,model.getId()+":"+
                            model.getProVarList().get(varSpinner.getSelectedItemPosition())
                                    .getId(),Toast.LENGTH_LONG).show();*/
                    boolean flag = db.delSingle(model.getId(),
                            model.getProVarList().get(varSpinner.getSelectedItemPosition()).getId());
                    if(flag){
                        QuantityConn conn = (QuantityConn) c;
                        conn.refreshAdapter();
                        addToList.setVisibility(View.VISIBLE);
                        qtyLayout.setVisibility(View.GONE);
                        //dismiss();
                    }
                }else{
                    model.setCustomerQty(qty-1);
                    model.setVarID(model.getProVarList().
                            get(varSpinner.getSelectedItemPosition()).getId());
                    db.update(model);
                    Toast.makeText(c,"Added to cart",Toast.LENGTH_SHORT).show();
                    proVarQty.setText(String.valueOf(qty-1));
                }
                new FlipAnimation(context,
                        String.valueOf(new AppDb(context).getCartCount())).start();
            }
        });

        varSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String []data = db.getProQtyIfAvaliable(model.getId(),model.getProVarList().get(i).getId());
                if(data != null){
                    PRO_AUTO_ID = Utils.convertToInt(data[0]);
                    qtyLayout.setVisibility(View.VISIBLE);
                    addToList.setVisibility(View.GONE);
                    proVarQty.setText(data[1]);
                }
                else{
                    PRO_AUTO_ID = 0;
                    qtyLayout.setVisibility(View.GONE);
                    addToList.setVisibility(View.VISIBLE);
                    proVarQty.setText("1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setUI() {
        db = new AppDb(c);

        title = this.findViewById(R.id.textView105);
        unit = this.findViewById(R.id.textView106);
        qtyEd = this.findViewById(R.id.textView107);
        addToList = this.findViewById(R.id.button8);
        close = this.findViewById(R.id.imageView41);
        itemRemove = this.findViewById(R.id.textView12);
        qtyLayout = this.findViewById(R.id.qtyLay);
        varSpinner = this.findViewById(R.id.textView117);
        plusBt = this.findViewById(R.id.button2);
        minusBt = this.findViewById(R.id.button);
        proVarQty = this.findViewById(R.id.textview23);
    }


    public void setModel(Product model){
        this.model = model;

        title.setText(model.getName());
        String []data = db.getProQtyIfAvaliable(model.getId(),model.getProVarList().get(0).getId());
        if(data != null){
            PRO_AUTO_ID = Utils.convertToInt(data[0]);
            qtyLayout.setVisibility(View.VISIBLE);
            addToList.setVisibility(View.GONE);
            proVarQty.setText(data[1]);
        }
        else{
            PRO_AUTO_ID = 0;
            qtyLayout.setVisibility(View.GONE);
            addToList.setVisibility(View.VISIBLE);
            proVarQty.setText("1");
        }

        //setting the spinner
        List<String>variantList = new ArrayList<>();
        for(ProductVarient v:model.getProVarList()) variantList.add("₹"+v.getPrice()+"/"+v.getName());
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(c,
                android.R.layout.simple_list_item_1, variantList);
        varSpinner.setAdapter(adp1);
    }




}

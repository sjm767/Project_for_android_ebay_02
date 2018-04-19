package proj.jaeshim.project_for_android_ebay_02;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvLotto;
    LottoAdapter lottoAdapter;
    EditText etLottoNo;
    int lastNo = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvLotto = findViewById(R.id.rvLotto);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvLotto.setLayoutManager(llm);
        rvLotto.setAdapter(lottoAdapter = new LottoAdapter());

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvLotto);

        etLottoNo = findViewById(R.id.etLottoNo);

        Button btQueryLotto = findViewById(R.id.btQueryLotto);

        btQueryLotto.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                String numStr = etLottoNo.getText().toString();
                etLottoNo.setText("");

                if (!TextUtils.isDigitsOnly(numStr)) {
                    Toast.makeText(MainActivity.this, "숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (TextUtils.isEmpty(numStr)) numStr = Integer.toString(lastNo);
                rvLotto.smoothScrollToPosition(Integer.parseInt(numStr) - 1);

                lottoAdapter.retrieveLottoData(numStr);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        rvLotto.scrollToPosition(lottoAdapter.getItemCount() - 1);
    }

    class LottoAdapter extends RecyclerView.Adapter<LottoAdapter.LottoViewHolder> {
        SparseArray<String> lottoInfoArray = new SparseArray<>();

        @NonNull
        @Override
        public LottoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View viewHolder = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.lotto_item, parent, false);

            return new LottoViewHolder(viewHolder);
        }

        @Override
        public void onBindViewHolder(@NonNull LottoViewHolder holder, int position) {
            String lottoString = lottoInfoArray.get(position + 1);
            if (lottoString != null) {
                holder.tvJsonData.setText(lottoString);
            } else {
                holder.tvJsonData.setText("retrieving...");
                retrieveLottoData(String.valueOf(position + 1));
            }
        }

        @Override
        public int getItemCount() {
            return 800;
        }

        final Callback<LottoService.NLottoResponse> cb = new Callback<LottoService.NLottoResponse>() {
            @Override
            public void onResponse(@NonNull Call<LottoService.NLottoResponse> call,
                                   @NonNull Response<LottoService.NLottoResponse> response) {
                LottoService.NLottoResponse body = response.body();

                if (body != null) {
                    lottoInfoArray.put(Integer.parseInt(body.drwNo),body.toString());
                    lottoAdapter.notifyItemChanged(lottoInfoArray.size()-1);
                    Log.i("TAG", body.toString());

                    Toast.makeText(MainActivity.this, body.drwNo + "회차의 로또 정보를 가져왔습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String err = "(unknown)";

                    try {
                        err = response.errorBody().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, "로또 정보 가져오기 실패: " + err, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LottoService.NLottoResponse> call,
                                  @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "로또 정보 가져오기 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        void retrieveLottoData(String position) {
            Call<LottoService.NLottoResponse> call = LottoService.getLottoNumbers(position);
//            call.execute(); // synchronous
            call.enqueue(cb); // asynchronous
        }

        class LottoViewHolder extends RecyclerView.ViewHolder {
            TextView tvJsonData;

            LottoViewHolder(View itemView) {
                super(itemView);

                tvJsonData = itemView.findViewById(R.id.tvJsonData);
            }
        }

    }
}
package tech.alvarez.contacts.listedit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.data.db.AppDatabase;
import tech.alvarez.contacts.data.db.entity.Person;
import tech.alvarez.contacts.edit.EditActivity;
import tech.alvarez.contacts.utils.Constants;

public class ListActivity extends AppCompatActivity implements ListContract.View, ListContract.OnItemClickListener, ListContract.DeleteListener {

    private ListContract.Presenter mPresenter;
    private PeopleAdapter mPeopleAdapter;

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // создаем кнопку и онкликлисенер
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // в презентере вызываем метод добавить персону
                mPresenter.addNewPerson();
            }
        });

        // ссылка на разметку пустого текста
        mEmptyTextView = (TextView) findViewById(R.id.emptyTextView);

        // код ресайклервью обычный
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPeopleAdapter = new PeopleAdapter(this);
        recyclerView.setAdapter(mPeopleAdapter);
// Объкт базы данных инициализирован и туда передана база данных из текушего приложения. не очень понял этот код.
        AppDatabase db = AppDatabase.getDatabase(getApplication());
        //презентеру назначен новый листпрезентер в который передан текущий Вью и ДАО
        mPresenter = new ListPresenter(this, db.personModel());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.populatePeople();
    }
    // переход в экран ввода нового человека
    @Override
    public void showAddPerson() {
        Intent intent = new Intent(this, EditActivity.class);
        startActivity(intent);
    }

    // метод делает невидимым поле текста для пустого списка и добавляет туда значение всего списка персон
    @Override
    public void setPersons(List<Person> persons) {
        mEmptyTextView.setVisibility(View.GONE);
        mPeopleAdapter.setValues(persons);
    }

    // переход в эдит активити, использование пут экстра - передача данных в следующий активити
    @Override
    public void showEditScreen(long id) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Constants.PERSON_ID, id);
        startActivity(intent);
    }

    // метод принимает объект персон, создает фрагмент, объявляет бандл, передает в бандл айд и персону, вызывает
    // в фрагементе сетАргумент в который передает набор пар айди и персон
    // показывает реализиацию другого метода по тегу
    @Override
    public void showDeleteConfirmDialog(Person person) {
        DeleteConfirmFragment fragment = new DeleteConfirmFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Constants.PERSON_ID, person.id);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(), "confirmDialog");
    }
    // показать пустое окно
    @Override
    public void showEmptyMessage() {
        mEmptyTextView.setVisibility(View.VISIBLE);
    }
    // устанавливаем презентер
    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    // объект Клика - принимает в себя персону, устанавливает что экран редактирования принимает в себя персону
    @Override
    public void clickItem(Person person) {
        mPresenter.openEditScreen(person);
    }

    // по длинному клику вызывает окошко подтверждения удаления
    @Override
    public void clickLongItem(Person person) {
        mPresenter.openConfirmDeleteDialog(person);
    }
    // подтверждено - если подтверждено (булеан) тогда презентер удаляет запись персонАйди
    @Override
    public void setConfirm(boolean confirm, long personId) {
        if (confirm) {
            mPresenter.delete(personId);
        }
    }
}

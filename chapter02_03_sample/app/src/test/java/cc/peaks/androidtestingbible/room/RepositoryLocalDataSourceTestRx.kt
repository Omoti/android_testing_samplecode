package cc.peaks.androidtestingbible.room

import android.arch.persistence.room.Room
import androidx.test.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RepositoryLocalDataSourceTestRx {
  lateinit var repositoryLocalDataSource: RepositoryLocalDataSource

  @Before
  fun setUp() {
    val context = InstrumentationRegistry.getTargetContext()
    val db = Room
        .databaseBuilder(context, AppDatabase::class.java, "repository-db")
        .allowMainThreadQueries() // just for test
        .build()
    repositoryLocalDataSource = RepositoryLocalDataSource(db)
  }

  @Test
  fun rx() {
    var list = repositoryLocalDataSource.findByOwnerRx("shiroyama")
        .test()
        .assertComplete()
        .values()[0]
    assertThat(list).isEmpty()

    val owner = "shiroyama"
    repositoryLocalDataSource.insertAll(
        Repository(1, "hello", "hello", owner),
        Repository(2, "world", "world", owner)
    )

    list = repositoryLocalDataSource.findByOwnerRx("shiroyama")
        .test()
        .assertComplete()
        .values()[0]
    assertThat(list).hasSize(2)
  }
}
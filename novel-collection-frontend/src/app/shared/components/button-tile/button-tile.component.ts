import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { FormControl } from '@angular/forms';
import { Reading } from 'src/app/model/user';
import { ReadingService } from 'src/app/service/reading-database.service';
import {Novel} from "../../../model/novel";

@Component({
  selector: 'button-tile',
  templateUrl: './button-tile.component.html',
  styleUrls: ['./button-tile.component.css']
})
export class ButtonTileComponent implements OnInit {

  @Input() reading: Reading;
  @Input() novel: Novel;
  @Output() onDeleted: EventEmitter<Reading> = new EventEmitter<Reading>();
  @Output() onChanged: EventEmitter<Reading> = new EventEmitter<Reading>();

  @Input() status: String[];
  selectedStatus : FormControl = new FormControl("Unread");
  selectedChapter: FormControl = new FormControl("");
  chapterList = Array;
  chapterCounter : number;

  constructor() { }

  ngOnInit(): void {
    this.chapterCounter = this.novel.lastReleasedChapter;

    this.selectedStatus.setValue(this.reading.status);
    if (this.reading.chapter != null) {
      this.selectedChapter.setValue(this.reading.chapter);
    }
  }

  isReading() : Boolean {
    return this.selectedStatus.value == 'Reading';
  }

  onChapter(chapter : number) {
    this.reading.chapter = chapter;
    this.onChanged.emit(this.reading);
  }

  onStatus(status : string) {
    this.reading.status = status;
    this.onChanged.emit(this.reading);
  }

  deleteClick(event: Reading) {
    this.onDeleted.emit(event);
  }
}

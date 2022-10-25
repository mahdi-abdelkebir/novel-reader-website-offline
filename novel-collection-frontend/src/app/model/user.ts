import { Chapter, Novel } from "./novel";

export class User {
  id: number;
  username: string;
  email: string;
  avatar: string;
  password: string;

  accessToken: string;
  roles: string[]
  novels: Reading[];
}

export class Reading {
  compositeKey: ReadingKey;
	status : string;
	chapter : number;
}

export class ReadingKey {
  novel: Novel
}

export class Bookmark {
  id: number;
  title: string;
  content: string;
  
  thoughts: string;
  tags: string;
  tagTable: string[];
  
  chapter: Chapter;
  chapterTitle: string;
  readmore: boolean = false;
}